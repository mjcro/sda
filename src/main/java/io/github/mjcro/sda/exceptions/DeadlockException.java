package io.github.mjcro.sda.exceptions;

import io.github.mjcro.sda.DatabaseException;
import org.jspecify.annotations.Nullable;

import java.sql.SQLException;

/**
 * Exception thrown when database encounters a deadlock.
 */
public class DeadlockException extends DatabaseException {
    public DeadlockException(@Nullable SQLException cause) {
        super(cause);
    }
}
