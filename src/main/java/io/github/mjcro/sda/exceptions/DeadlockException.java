package io.github.mjcro.sda.exceptions;

import io.github.mjcro.sda.DatabaseException;

import java.sql.SQLException;

/**
 * Exception thrown when database encounters a deadlock.
 */
public class DeadlockException extends DatabaseException {
    public DeadlockException(SQLException cause) {
        super(cause);
    }
}
