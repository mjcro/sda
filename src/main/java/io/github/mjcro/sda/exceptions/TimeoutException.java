package io.github.mjcro.sda.exceptions;

import io.github.mjcro.sda.DatabaseException;
import org.jspecify.annotations.Nullable;

import java.sql.SQLException;

public class TimeoutException extends DatabaseException {
    public TimeoutException(@Nullable SQLException cause) {
        super(cause);
    }
}
