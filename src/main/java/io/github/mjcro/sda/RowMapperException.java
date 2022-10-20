package io.github.mjcro.sda;

public class RowMapperException extends DatabaseException {
    public RowMapperException(final String message) {
        super(message);
    }

    public RowMapperException(final Class<?> clazz) {
        this(String.format("No suitable mapper for clazz %s", clazz.getName()));
    }
}
