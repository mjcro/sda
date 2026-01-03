package io.github.mjcro.sda.exceptions;

import io.github.mjcro.sda.DatabaseException;
import org.jspecify.annotations.Nullable;

import java.sql.SQLException;

/**
 * Exception thrown when database encounters constrain violation.
 */
public class ConstraintViolationException extends DatabaseException {
    public ConstraintViolationException(@Nullable SQLException cause) {
        super(cause);
    }
}
