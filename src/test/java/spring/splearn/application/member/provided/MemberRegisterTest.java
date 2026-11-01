package spring.splearn.application.member.provided;


import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import spring.splearn.SplearnTestConfiguration;
import spring.splearn.application.member.provided.MemberRegister;
import spring.splearn.domain.member.DuplicateEmailException;
import spring.splearn.domain.member.Member;
import spring.splearn.domain.member.MemberRegisterRequest;
import spring.splearn.domain.member.MemberStatus;

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
        Member member = memberRegister.register(createMemberRegisterRequest());
        entityManager.flush();
        entityManager.clear();

        memberRegister.activate(member.getId());
        entityManager.flush();
    }

    private void extracted(MemberRegisterRequest invalid) {
        assertThatThrownBy(() -> memberRegister.register(invalid))
            .isInstanceOf(ConstraintViolationException.class);
    }
}