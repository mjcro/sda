package io.github.mjcro.sda.exceptions;

import io.github.mjcro.sda.DatabaseException;
import org.jspecify.annotations.Nullable;

import java.sql.SQLException;

/**
 * Thrown when the database aborts a transaction due to a deadlock or lock-wait timeout.
 * Typically retryable.
 */
public class DeadlockException extends DatabaseException {
    /**
     * @param cause Original JDBC exception. May be null.
     */
    public DeadlockException(@Nullable SQLException cause) {
        super(cause);
    }
}
