package io.github.mjcro.sda.reflection;

import io.github.mjcro.sda.FieldWriter;
import org.jspecify.annotations.NonNull;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
interface FieldWriterReflective<T> extends FieldWriter<T> {
    @Override
    default void write(@NonNull ResultSet from, @NonNull T to) throws SQLException {
        try {
            write0(from, to);
        } catch (ReflectiveOperationException e) {
            throw new SQLException(e);
        }
    }

    void write0(@NonNull ResultSet from, @NonNull T to) throws SQLException, ReflectiveOperationException;
}
