package io.github.mjcro.sda;

import org.jspecify.annotations.NonNull;

import java.sql.ResultSet;
import java.sql.SQLException;

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
