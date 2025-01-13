package io.github.mjcro.sda;

import java.util.Objects;

/**
 * Simple statement implementation that contains SQL query only
 * and no parameters.
 */
class StatementQueryOnly implements Statement {
    private static final Object[] parameters = new Object[0];
    private final String sql;

    StatementQueryOnly(String sql) {
        this.sql = Objects.requireNonNull(sql, "sql");
    }

    @Override
    public String getSql() {
        return sql;
    }

    @Override
    public Object[] getParameters() {
        return parameters;
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
