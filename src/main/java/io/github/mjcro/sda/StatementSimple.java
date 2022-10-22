package io.github.mjcro.sda;

import java.util.Objects;

/**
 * Simple statement implementation that has both query and placeholders.
 */
class StatementSimple implements Statement {
    private final String sql;
    private final Object[] placeholders;

    StatementSimple(String sql, Object[] placeholders) {
        this.sql = Objects.requireNonNull(sql, "sql");
        this.placeholders = Objects.requireNonNull(placeholders, "placeholders");
    }

    @Override
    public String getSql() {
        return sql;
    }

    @Override
    public Object[] getPlaceholders() {
        return placeholders;
    }

    @Override
    public int hashCode() {
        return Statement.basicHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Statement && Statement.basicallyEquals(this, (Statement) obj);
    }

    @Override
    public String toString() {
        return this.sql;
    }
}
