package io.github.mjcro.sda;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

/**
 * Thrown when a {@link RowMapper} cannot be resolved or fails while mapping a row.
 */
public class RowMapperException extends DatabaseException {
    /**
     * @param message Diagnostic message. May be null.
     */
    public RowMapperException(@Nullable String message) {
        super(message);
    }

    /**
     * @param clazz Target class for which no mapper was found. Not null.
     */
    public RowMapperException(@NonNull Class<?> clazz) {
        super(String.format("No suitable mapper for clazz %s", clazz.getName()));
    }

    /**
     * @param clazz Target class for which no mapper was found. Not null.
     * @param cause Underlying cause. Not null.
     */
    public RowMapperException(@NonNull Class<?> clazz, @NonNull Throwable cause) {
        super(String.format("No suitable mapper for clazz %s", clazz.getName()));
    }
}
