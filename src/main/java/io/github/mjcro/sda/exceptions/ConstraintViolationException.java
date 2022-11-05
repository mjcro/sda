package io.github.mjcro.sda.exceptions;

import io.github.mjcro.sda.DatabaseException;

import java.sql.SQLException;

/**
 * Exception thrown when database encounters constrain violation.
 */
public class ConstraintViolationException extends DatabaseException {
    public ConstraintViolationException(SQLException cause) {
        super(cause);
    }
}
