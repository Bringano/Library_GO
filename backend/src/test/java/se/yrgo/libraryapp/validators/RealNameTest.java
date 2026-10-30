package se.yrgo.libraryapp.validators;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

public class RealNameTest {

    @ParameterizedTest
    @ValueSource(strings = {"Bosse", "Bosse Bula", "Åsa Öberg"})
    void namesWithoutBadWordsAreAccepted(String name) {
        assertThat(RealName.validate(name)).isTrue();
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {
            "Bosse shiet",
            "Bosse jäkla",
            "shiet Bosse",
            "shiet",
            "Bosse FREAKING",
            "Bosse fr34king"
    })
    void namesWithBadWordsAreRejected(String name) {
        assertThat(RealName.validate(name)).isFalse();
    }
}