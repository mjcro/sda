package io.github.mjcro.sda.reflection;

import io.github.mjcro.sda.DatabaseException;

public class DatabaseReflectiveOperationException extends DatabaseException {
    public DatabaseReflectiveOperationException(final Exception cause) {
        super(cause);
    }

    public DatabaseReflectiveOperationException(final String cause) {
        super(cause);
    }
}
