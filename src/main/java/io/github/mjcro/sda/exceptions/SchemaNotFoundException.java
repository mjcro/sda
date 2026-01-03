package io.github.mjcro.sda.exceptions;

import io.github.mjcro.sda.DatabaseException;
import org.jspecify.annotations.Nullable;

import java.sql.SQLException;

/**
 * Exception thrown while accessing non-existent table.
 */
public class SchemaNotFoundException extends DatabaseException {
    public SchemaNotFoundException(@Nullable SQLException cause) {
        super(cause);
    }
}
