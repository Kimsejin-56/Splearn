package spring.splearn.adapter.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;
import spring.splearn.domain.Email;

class DummyEmailSenderTest {

    @Test
    @StdIo
    void dummyEmailSender(StdOut out){
        DummyEmailSender dummyEmailSender = new DummyEmailSender();

        dummyEmailSender.send(new Email("sj@naver.com"), "subject", "body");

        Assertions.assertThat(out.capturedLines()[0]).isEqualTo("DummyEmailSender send email: Email[address=sj@naver.com]");
    }
}