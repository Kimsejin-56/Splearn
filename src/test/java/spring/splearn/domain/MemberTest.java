package spring.splearn.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class MemberTest {
    @Test
    void createMember(){
        Member member = new Member("SJ@splearn.app", "SJ", "secret");

        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }
}