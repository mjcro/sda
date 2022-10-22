package io.github.mjcro.sda;

import java.sql.SQLException;

public class DatabaseException extends RuntimeException {
    public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException(SQLException cause) {
        super(cause);
    }

    public DatabaseException(Exception cause) {
        super(cause);
    }
}
