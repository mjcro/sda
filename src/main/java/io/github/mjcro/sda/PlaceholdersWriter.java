package io.github.mjcro.sda;

import io.github.mjcro.writers.Writer;
import org.jspecify.annotations.NonNull;

import java.io.IOException;

/**
 * Writer writing placeholders like ?,?,?
 */
public class PlaceholdersWriter implements Writer<Integer> {
    static final PlaceholdersWriter QUESTIONS = new PlaceholdersWriter("?");

    private final String character;

    /**
     * Constructor.
     *
     * @param character Placeholder character.
     */
    public PlaceholdersWriter(String character) {
        this.character = character;
    }

    @Override
    public void writeTo(@NonNull Appendable to, Integer n) throws IOException {
        if (n == null || n < 1) {
            return;
        }

        for (int i = 0; i < n; i++) {
            if (i > 0) {
                to.append(',');
            }
            to.append(character);
        }
    }
}
