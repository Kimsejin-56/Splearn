package spring.splearn.adapter.security;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class SecurePasswordEncoderTest {

    @Test
    void securePasswordEncoder(){
        SecurePasswordEncoder securePasswordEncoder = new SecurePasswordEncoder();

        String passwordHash = securePasswordEncoder.encode("password");

        Assertions.assertThat(securePasswordEncoder.matches("password", passwordHash)).isTrue();
        Assertions.assertThat(securePasswordEncoder.matches("password123", passwordHash)).isFalse();
    }
}