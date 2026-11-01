package spring.splearn.domain.member;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class ProfileTest {
    @Test
    void profile(){
        new Profile("dadad");
        new Profile("dadad213");
        new Profile("213");
    }

    @Test
    void profileFail(){
        assertThatThrownBy(() -> new Profile(""))
            .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> new Profile("toolongtoolongtoolongtoolongtoolongtoolong"))
            .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> new Profile("A"))
            .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> new Profile("프로필"))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void url() {
        Profile profile = new Profile("dadad");
        assertThat(profile.url()).isEqualTo("@dadad");
    }
}