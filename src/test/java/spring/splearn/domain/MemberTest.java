package spring.splearn.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class MemberTest {
    @Test
    void createMember(){
        Member member = new Member("SJ@splearn.app", "SJ", "secret");

        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

    @Test
    void constructorNullCheck(){
        assertThatThrownBy(()-> new Member(null, "SJ", "secret"))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void activate(){
        Member member = new Member("SJ@splearn.app", "SJ", "secret");

        member.activate();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
    }
    
    @Test
    void activateFail(){
        Member member = new Member("SJ@splearn.app", "SJ", "secret");

        member.activate();

        assertThatThrownBy(() ->{
            member.activate();
        }).isInstanceOf(IllegalStateException.class);
    }
    
    @Test
    void deactivate(){
        Member member = new Member("SJ@splearn.app", "SJ", "secret");
        member.activate();

        member.deactivate();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.DEACTIVATED);
    }

    @Test
    void dectivateFail(){
        Member member = new Member("SJ@splearn.app", "SJ", "secret");

        assertThatThrownBy(() ->{
            member.deactivate();
        }).isInstanceOf(IllegalStateException.class);

        member.activate();
        member.deactivate();

        assertThatThrownBy(() ->{
            member.deactivate();
        }).isInstanceOf(IllegalStateException.class);
    }
}