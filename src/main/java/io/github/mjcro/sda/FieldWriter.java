package io.github.mjcro.sda;

import org.jspecify.annotations.NonNull;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Writes a single value, read from a {@link ResultSet}, into a target object.
 *
 * @param <T> Target object type.
 */
public interface FieldWriter<T> {
    /**
     * Writes data from result set to target object.
     *
     * @param from Source result set.
     * @param to   Target object.
     * @throws SQLException On write error.
     */
    void write(@NonNull ResultSet from, @NonNull T to) throws SQLException;
}
