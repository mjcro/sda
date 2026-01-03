package io.github.mjcro.sda;

import org.jspecify.annotations.Nullable;

import java.sql.SQLException;

public class DatabaseException extends RuntimeException {
    public DatabaseException(@Nullable String message) {
        super(message);
    }

    public DatabaseException(@Nullable SQLException cause) {
        super(cause);
    }

    public DatabaseException(@Nullable Exception cause) {
        super(cause);
    }
}
