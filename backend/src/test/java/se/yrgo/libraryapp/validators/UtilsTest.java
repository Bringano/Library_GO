package se.yrgo.libraryapp.validators;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.assertj.core.api.Assertions.assertThat;

public class UtilsTest {

    @ParameterizedTest
    @CsvFileSource(resources = "/testdata/only_letters_and_whitespace.csv", numLinesToSkip = 1)
    void onlyLettersAndWhitespaceRemovesNonLettersAndLowercases(String input, String expected) {
        assertThat(Utils.onlyLettersAndWhitespace(input)).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/testdata/clean_and_unleet.csv", numLinesToSkip = 1)
    void cleanAndUnLeetConvertsLeetAndRemovesNonLetters(String input, String expected) {
        assertThat(Utils.cleanAndUnLeet(input)).isEqualTo(expected);
    }
}
