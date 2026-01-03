package io.github.mjcro.sda.reflection;

import io.github.mjcro.sda.DatabaseException;
import org.jspecify.annotations.Nullable;

public class DatabaseReflectiveOperationException extends DatabaseException {
    public DatabaseReflectiveOperationException(@Nullable Exception cause) {
        super(cause);
    }

    public DatabaseReflectiveOperationException(@Nullable String cause) {
        super(cause);
    }
}
