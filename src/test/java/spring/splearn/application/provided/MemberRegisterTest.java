package spring.splearn.application.provided;


import jakarta.persistence.EntityManager;
import jakarta.validation.Valid;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import spring.splearn.SplearnTestConfiguration;
import spring.splearn.domain.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static spring.splearn.domain.MemberFixture.createMemberRegisterRequest;

@SpringBootTest
@Transactional
@Valid
@Import(SplearnTestConfiguration.class)
public record MemberRegisterTest(MemberRegister memberRegister, EntityManager entityManager) {

    @Test
    void register(){
        Member member = memberRegister.register(createMemberRegisterRequest());

        assertThat(member.getId()).isNotNull();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

    @Test
    void duplicateEmailFail(){
        Member member = memberRegister.register(createMemberRegisterRequest());

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