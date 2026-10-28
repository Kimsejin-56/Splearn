package spring.splearn.application.provided;

import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.util.ReflectionTestUtils;
import spring.splearn.application.MemberService;
import spring.splearn.application.required.EmailSender;
import spring.splearn.application.required.MemberRepository;
import spring.splearn.domain.Email;
import spring.splearn.domain.Member;
import spring.splearn.domain.MemberStatus;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static spring.splearn.domain.MemberFixture.createMemberRegisterRequest;
import static spring.splearn.domain.MemberFixture.createPasswordEncoder;

@DataJpaTest
class MemberRegisterTest {

    @Test
    void registerTestStub(){
        MemberRegister register =
                new MemberService(new MemberRepositoryStub(), new EmailSenderStub(), createPasswordEncoder());

        Member member = register.register(createMemberRegisterRequest());

        assertThat(member.getId()).isNotNull();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

    @Test
    void registerTestMock(){
        EmailSenderMock emailSenderMock = new EmailSenderMock();
        MemberRegister register =
                new MemberService(new MemberRepositoryStub(), emailSenderMock, createPasswordEncoder());

        Member member = register.register(createMemberRegisterRequest());

        assertThat(member.getId()).isNotNull();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);

        assertThat(emailSenderMock.getTos()).hasSize(1);
        assertThat(emailSenderMock.getTos().getFirst()).isEqualTo(member.getEmail());
    }

    @Test
    void registerTestMockito(){
        EmailSender emailSenderMock = mock(EmailSender.class);
        MemberRegister register =
                new MemberService(new MemberRepositoryStub(), emailSenderMock, createPasswordEncoder());

        Member member = register.register(createMemberRegisterRequest());

        assertThat(member.getId()).isNotNull();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);

        verify(emailSenderMock).send(eq(member.getEmail()), any(), any());
    }

    static class MemberRepositoryStub implements MemberRepository {
        @Override
        public Member save(Member member) {
            ReflectionTestUtils.setField(member, "id", 1L);
            return member;
        }
    }

    static class EmailSenderStub implements EmailSender {
        @Override
        public void send(Email email, String subject, String body) {}
    }

    @Getter
    static class EmailSenderMock implements EmailSender {
        List<Email> tos=new ArrayList<>();

        @Override
        public void send(Email email, String subject, String body) {
            tos.add(email);
        }
    }


}