package se.yrgo.libraryapp.validators;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This validator checks that the real names match our high standard for proper names. I.e. no bad
 * words.
 */
public final class RealName {
    private static Logger logger = LoggerFactory.getLogger(RealName.class);
    private static final Set<String> invalidWords = new HashSet<>();

    static {
        try (InputStream is = RealName.class.getClassLoader().getResourceAsStream("bad_words.txt");
             BufferedReader reader =
                     new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                invalidWords.add(line);
            }
        } catch (IOException ex) {
            logger.error("Unable to initialize list of bad words", ex);
        }
    }

    private RealName() {
    }

    /**
     * Validates if the given name is a valid and proper name.
     *
     * @param name the name to check
     * @return true if valid, false if not
     *
     */
    public static boolean validate(String name) {
        if (name == null) {
            return false;
        }
        String cleanName = Utils.cleanAndUnLeet(name);
        String[] words = cleanName.split("\\s+");
        for (int i = 0; i < words.length; i++) {
            if (invalidWords.contains(words[i])) {
                return false;
            }
        }
        return true;
    }
}
