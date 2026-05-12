package io.github.mjcro.sda;

import org.jspecify.annotations.Nullable;

import java.sql.SQLException;

/**
 * Common unchecked superclass for all database-access failures raised by this library.
 * Dialect-specific subclasses may convey more precise error semantics (deadlock, timeout, etc.).
 */
public class DatabaseException extends RuntimeException {
    /**
     * @param message Diagnostic message. May be null.
     */
    public DatabaseException(@Nullable String message) {
        super(message);
    }

    /**
     * @param cause Original JDBC exception. May be null.
     */
    public DatabaseException(@Nullable SQLException cause) {
        super(cause);
    }

    /**
     * @param cause Wrapped underlying cause. May be null.
     */
    public DatabaseException(@Nullable Exception cause) {
        super(cause);
    }
}
