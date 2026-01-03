package io.github.mjcro.sda;

import org.jspecify.annotations.Nullable;

public class UnsupportedDialectException extends DatabaseException {
    public UnsupportedDialectException(@Nullable Dialect dialect) {
        super("Unsupported dialect " + (dialect == null ? "null" : dialect.getName()));
    }
}
