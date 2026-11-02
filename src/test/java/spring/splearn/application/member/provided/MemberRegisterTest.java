package spring.splearn.application.member.provided;


import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import spring.splearn.SplearnTestConfiguration;
import spring.splearn.domain.member.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static spring.splearn.domain.member.MemberFixture.createMemberRegisterRequest;

@SpringBootTest
@Transactional
@Valid
@Import(SplearnTestConfiguration.class)
record MemberRegisterTest(MemberRegister memberRegister, EntityManager entityManager) {

    @Test
    void register(){
        Member member = memberRegister.register(createMemberRegisterRequest());

        System.out.println(member);

        assertThat(member.getId()).isNotNull();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

    @Test
    void duplicateEmailFail(){
        memberRegister.register(createMemberRegisterRequest());

        assertThatThrownBy(() -> memberRegister.register(createMemberRegisterRequest()))
                .isInstanceOf(DuplicateEmailException.class);
    }

    @Test
    void memberRegisterRequestFail(){
        extracted(new MemberRegisterRequest("SJ@splearn.app", "SJ", "verysecret"));
        extracted(new MemberRegisterRequest("SJ@splearn.app", "SJ-------", "secret"));
        extracted(new MemberRegisterRequest("SJsplearn.app", "SJ--------", "verysecret"));
    }
    
    @Test
    void activate(){
        Member member = registerMember();

        member = memberRegister.activate(member.getId());
        entityManager.flush();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
        assertThat(member.getDetail().getActivatedAt()).isNotNull();
    }

    @Test
    void deactivate(){
        Member member = registerMember();

        member = memberRegister.activate(member.getId());
        entityManager.flush();
        entityManager.clear();

        member = memberRegister.deactivate(member.getId());

        assertThat(member.getStatus()).isEqualTo(MemberStatus.DEACTIVATED);
        assertThat(member.getDetail().getDeactivatedAt()).isNotNull();
    }

    @Test
    void updateInfo(){
        Member member = registerMember();

        member = memberRegister.activate(member.getId());
        entityManager.flush();
        entityManager.clear();

        member = memberRegister.updateInfo(member.getId(), new MemberInfoUpdateRequest("peter", "profile", "Hi"));

        assertThat(member.getDetail().getProfile().address()).isEqualTo("profile");
    }

    @Test
    void updateInfoFail(){
        Member member = registerMember();
        memberRegister.activate(member.getId());
        memberRegister.updateInfo(member.getId(), new MemberInfoUpdateRequest("peter", "profile", "Hi"));

        Member member2 = registerMember("SJ@naver.com");
        memberRegister.activate(member2.getId());
        entityManager.flush();
        entityManager.clear();

        assertThatThrownBy(() -> memberRegister.updateInfo(member2.getId(),  new MemberInfoUpdateRequest("peter2", "profile", "Hi")))
            .isInstanceOf(DuplicateProfileException.class);

        memberRegister.updateInfo(member2.getId(), new MemberInfoUpdateRequest("peter2", "profile2", "Hi"));
        memberRegister.updateInfo(member2.getId(), new MemberInfoUpdateRequest("peter2", "", "Hi"));
        memberRegister.updateInfo(member2.getId(), new MemberInfoUpdateRequest("peter2", "profile2", "Hi"));

        assertThatThrownBy(() -> memberRegister.updateInfo(member.getId(),  new MemberInfoUpdateRequest("peter2", "profile2", "Hi")))
                .isInstanceOf(DuplicateProfileException.class);
    }

    private Member registerMember() {
        Member member = memberRegister.register(createMemberRegisterRequest());
        entityManager.flush();
        entityManager.clear();
        return member;
    }

    private Member registerMember(String email) {
        Member member = memberRegister.register(createMemberRegisterRequest(email));
        entityManager.flush();
        entityManager.clear();
        return member;
    }

    private void extracted(MemberRegisterRequest invalid) {
        assertThatThrownBy(() -> memberRegister.register(invalid))
            .isInstanceOf(ConstraintViolationException.class);
    }
}