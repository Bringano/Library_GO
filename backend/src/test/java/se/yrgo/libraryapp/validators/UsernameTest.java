package se.yrgo.libraryapp.validators;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UsernameTest {
    @Test
    void correctUsername() {
        assertTrue(Username.validate("bosse"));
    }

    @Test
    void incorrectUsername() {
        assertFalse(Username.validate("name with space"));
    }
}
