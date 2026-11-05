package se.yrgo.libraryapp.validators;

import net.jqwik.api.*;

public class UtilsPropertyTest {

    @Property
    boolean resultIsOnlyLowercaseAndWhitespace(@ForAll String text) {
        String result = Utils.onlyLettersAndWhitespace(text);

        for (int i = 0; i < result.length(); i++) {
            char ch = result.charAt(i);

            boolean isWhitespace = Character.isWhitespace(ch);
            boolean isSmallLetter = Character.isLetter(ch) && Character.toLowerCase(ch) == ch;

            if (!isWhitespace && !isSmallLetter) {
                return false;
            }
        }
        return true;
    }

    @Provide
    Arbitrary<String> textWithLeet() {
        return Arbitraries.strings().withChars("abcdefghijklmnopqrstuvwxyz 01345678");
    }

    @Property
    boolean noLeetDigitsLeft(@ForAll("textWithLeet") String text) {
        String result = Utils.cleanAndUnLeet(text);

        for (int i = 0; i < result.length(); i++) {
            char ch = result.charAt(i);
            if (Character.isDigit(ch)) {
                return false;
            }
        }
        return true;
    }

    @Provide
    Arbitrary<String> onlyLeet() {
        return Arbitraries.strings().withChars("01345678");
    }

    @Property
    boolean everyLeetDigitBecomesALetter(@ForAll("onlyLeet") String text) {
        String result = Utils.cleanAndUnLeet(text);
        return result.length() == text.length();
    }
}
