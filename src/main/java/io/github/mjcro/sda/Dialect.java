package io.github.mjcro.sda;

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
    default boolean isCompatibleWith(Dialect other) {
        return other == this;
    }

    /**
     * Converts exception into compatible.
     *
     * @param source Source exception.
     * @return Resulting exception.
     */
    default DatabaseException convertException(SQLException source) {
        return new DatabaseException(source);
    }
}
