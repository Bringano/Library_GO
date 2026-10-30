package se.yrgo.libraryapp.validators;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;


public class UsernameTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "bosse",
            "BOSSE",
            "Gust4v",
            "user0123456789",
            "1234",
            "Gu-stav",
            "Gust_av",
            "Gust.av",
            "Jax@",
            "@._-",
            "goddamnthisusernamneislonglonglonglonglong"
    })
    void validUsernamesAreAccepted(String name) {
        assertThat(Username.validate(name)).isTrue();
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    @ValueSource(strings = {
            "Jax",
            "Gustav Jr",
            "    ",
            " Gustav",
            "Gustav ",
            "Gustav:)",
            "Gustäv",
            "Carl-Gustäv"
    })
    void invalidUsernamesAreRejected(String name) {
        assertThat(Username.validate(name)).isFalse();
    }
}
