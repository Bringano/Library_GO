package se.yrgo.libraryapp.validators;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class UsernameTest {

    @Test
    void correctUsername() {
        assertThat(Username.validate("bosse")).isTrue();
    }

    @Test
    void incorrectUsername() {
        assertThat(Username.validate("name with space")).isFalse();
    }
}
