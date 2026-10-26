package spring.splearn.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EmailTest {
    
    @Test
    void equality(){
        Email email1 = new Email("sj1234@naver.com");
        Email email2 = new Email("sj1234@naver.com");

        assertThat(email1).isEqualTo(email2);
    }
}