package spring.splearn.application.provided;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import spring.splearn.application.required.MemberRepository;
import spring.splearn.domain.Member;

import static org.assertj.core.api.Assertions.assertThat;
import static spring.splearn.domain.MemberFixture.createMemberRegisterRequest;
import static spring.splearn.domain.MemberFixture.createPasswordEncoder;

@DataJpaTest
class MemberRegisterTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EntityManager em;

    @Test
    void registerMember(){
        Member member = Member.register(createMemberRegisterRequest(), createPasswordEncoder());

        assertThat(member.getId()).isNull();

        memberRepository.save(member);

        assertThat(member.getId()).isNotNull();

        em.flush();
    }
}