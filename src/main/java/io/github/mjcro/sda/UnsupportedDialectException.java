package io.github.mjcro.sda;

import org.jspecify.annotations.Nullable;

/**
 * Thrown when an operation cannot be performed against the requested {@link Dialect}.
 */
public class UnsupportedDialectException extends DatabaseException {
    /**
     * @param dialect Dialect that is not supported. May be null.
     */
    public UnsupportedDialectException(@Nullable Dialect dialect) {
        super("Unsupported dialect " + (dialect == null ? "null" : dialect.getName()));
    }
}
