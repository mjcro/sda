package io.github.mjcro.sda.exceptions;

import io.github.mjcro.sda.DatabaseException;
import org.jspecify.annotations.Nullable;

import java.sql.SQLException;

/**
 * Thrown when a query references a non-existent schema, table or view.
 */
public class SchemaNotFoundException extends DatabaseException {
    /**
     * @param cause Original JDBC exception. May be null.
     */
    public SchemaNotFoundException(@Nullable SQLException cause) {
        super(cause);
    }
}
