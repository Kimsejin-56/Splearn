package spring.splearn.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class MemberTest {
    Member member;
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        passwordEncoder = new PasswordEncoder() {
            @Override
            public String encode(String password) {
                return password.toUpperCase();
            }

            @Override
            public boolean matches(String password, String passwordHash) {
                return passwordEncoder.encode(password).equals(passwordHash);
            }
        };

        member=Member.register(new MemberRegisterRequest("SJ@splearn.app", "SJ", "secret"), passwordEncoder);
    }

    @Test
    void registerMember(){
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

    @Test
    void activate(){
        member.activate();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
    }
    
    @Test
    void activateFail(){
        member.activate();

        assertThatThrownBy(() ->{
            member.activate();
        }).isInstanceOf(IllegalStateException.class);
    }
    
    @Test
    void deactivate(){
        member.activate();

        member.deactivate();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.DEACTIVATED);
    }

    @Test
    void dectivateFail(){
        assertThatThrownBy(() ->{
            member.deactivate();
        }).isInstanceOf(IllegalStateException.class);

        member.activate();
        member.deactivate();

        assertThatThrownBy(() ->{
            member.deactivate();
        }).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void verifyPassword(){
        assertThat(member.verifyPassword("secret", passwordEncoder)).isTrue();
        assertThat(member.verifyPassword("hello", passwordEncoder)).isFalse();
    }

    @Test
    void changeNickname() {
        assertThat(member.getNickname()).isEqualTo("SJ");

        member.changeNickname("chSJ");

        assertThat(member.getNickname()).isEqualTo("chSJ");
    }

    @Test
    void changePassword(){
        member.changePassword("verysecret", passwordEncoder);

        assertThat(member.verifyPassword("verysecret", passwordEncoder)).isTrue();
    }
    
    @Test
    void isActive(){
        assertThat(member.isActive()).isFalse();

        member.activate();

        assertThat(member.isActive()).isTrue();

        member.deactivate();

        assertThat(member.isActive()).isFalse();
    }

    @Test
    void invalidEmail(){
        assertThatThrownBy(() ->
                Member.register(new MemberRegisterRequest("invalid Email", "SJ", "secret"), passwordEncoder)
        ).isInstanceOf(IllegalArgumentException.class);

        Member.register(new MemberRegisterRequest("sj1234@naver.com", "SJ", "secret"), passwordEncoder);
    }
}