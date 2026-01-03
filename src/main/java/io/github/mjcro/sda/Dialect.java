package io.github.mjcro.sda;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.sql.SQLException;

/**
 * Defines database dialect.
 * Used in {@link StatementPrototype} to generate statements;
 */
@FunctionalInterface
public interface Dialect {
    Dialect H2 = new DialectH2();
    Dialect MySQL = new DialectMySql();

    String getName();

    /**
     * Compares compatibilities of two dialects.
     *
     * @param other Dialect to compare with;
     * @return True if both dialects are same or compatible.
     */
    default boolean isCompatibleWith(@Nullable Dialect other) {
        return other == this;
    }

    /**
     * Converts exception into compatible.
     *
     * @param source Source exception.
     * @return Resulting exception.
     */
    default @NonNull DatabaseException convertException(@NonNull SQLException source) {
        return new DatabaseException(source);
    }

    /**
     * Prepares parameter before using it in queries.
     *
     * @param value Parameter value.
     * @return Prepared value.
     */
    default @Nullable Object prepareParameters(@Nullable Object value) {
        return value;
    }
}
