package io.github.mjcro.sda.exceptions;

import io.github.mjcro.sda.DatabaseException;

import java.sql.SQLException;

/**
 * Exception thrown while accessing non-existent table.
 */
public class SchemaNotFoundException extends DatabaseException {
    public SchemaNotFoundException(SQLException cause) {
        super(cause);
    }
}
