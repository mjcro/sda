package io.github.mjcro.sda;

public class UnsupportedDialectException extends DatabaseException {
    public UnsupportedDialectException(Dialect dialect) {
        super("Unsupported dialect " + (dialect == null ? "null" : dialect.getName()));
    }
}
