package io.github.mjcro.sda;

import java.util.Objects;

/**
 * Simple statement implementation that has both query and parameters.
 */
class StatementSimple implements Statement {
    private final String sql;
    private final Object[] parameters;

    StatementSimple(String sql, Object[] parameters) {
        this.sql = Objects.requireNonNull(sql, "sql");
        this.parameters = Objects.requireNonNull(parameters, "parameters");
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
