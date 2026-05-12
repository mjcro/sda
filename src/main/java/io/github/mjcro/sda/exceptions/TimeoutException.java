package io.github.mjcro.sda.exceptions;

import io.github.mjcro.sda.DatabaseException;
import org.jspecify.annotations.Nullable;

import java.sql.SQLException;

/**
 * Thrown when a query is aborted by the database server because it exceeded the maximum execution time.
 */
public class TimeoutException extends DatabaseException {
    /**
     * @param cause Original JDBC exception. May be null.
     */
    public TimeoutException(@Nullable SQLException cause) {
        super(cause);
    }
}
