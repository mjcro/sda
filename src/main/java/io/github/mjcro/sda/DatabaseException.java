package io.github.mjcro.sda;

import java.sql.SQLException;

public class DatabaseException extends RuntimeException {
    protected DatabaseException(String message) {
        super(message);
    }

    public DatabaseException(final SQLException cause) {
        super(cause);
    }

    public DatabaseException(final Exception cause) {
        super(cause);
    }
}
