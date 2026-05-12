package io.github.mjcro.sda.reflection;

import org.jspecify.annotations.NonNull;

import java.sql.SQLException;

/**
 * Thrown when a SQL NULL is read into an unboxed primitive scalar field where null cannot be represented.
 */
public class ScalarNullException extends SQLException {
    /**
     * @param type Primitive type name (e.g. {@code "int"}, {@code "long"}). Not null.
     */
    public ScalarNullException(@NonNull String type) {
        super("Got NULL for scalar " + type);
    }
}
