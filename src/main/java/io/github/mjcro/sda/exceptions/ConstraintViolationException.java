package io.github.mjcro.sda.exceptions;

import io.github.mjcro.sda.DatabaseException;
import org.jspecify.annotations.Nullable;

import java.sql.SQLException;

/**
 * Thrown when a write violates a database constraint (unique index, foreign key, etc.).
 */
public class ConstraintViolationException extends DatabaseException {
    /**
     * @param cause Original JDBC exception. May be null.
     */
    public ConstraintViolationException(@Nullable SQLException cause) {
        super(cause);
    }
}
