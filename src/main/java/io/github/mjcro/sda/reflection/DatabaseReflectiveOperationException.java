package io.github.mjcro.sda.reflection;

import io.github.mjcro.sda.DatabaseException;

public class DatabaseReflectiveOperationException extends DatabaseException {
    public DatabaseReflectiveOperationException(Exception cause) {
        super(cause);
    }

    public DatabaseReflectiveOperationException(String cause) {
        super(cause);
    }
}
