package io.github.mjcro.sda;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class RowMapperException extends DatabaseException {
    public RowMapperException(@Nullable String message) {
        super(message);
    }

    public RowMapperException(@NonNull Class<?> clazz) {
        super(String.format("No suitable mapper for clazz %s", clazz.getName()));
    }

    public RowMapperException(@NonNull Class<?> clazz, @NonNull Throwable cause) {
        super(String.format("No suitable mapper for clazz %s", clazz.getName()));
    }
}
