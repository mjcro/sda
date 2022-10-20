package io.github.mjcro.sda;

public class RowMapperException extends DatabaseException {
    public RowMapperException(String message) {
        super(message);
    }

    public RowMapperException(Class<?> clazz) {
        this(String.format("No suitable mapper for clazz %s", clazz.getName()));
    }
}
