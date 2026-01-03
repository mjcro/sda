package io.github.mjcro.sda;

import io.github.mjcro.interfaces.database.Statement;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * Simple statement implementation that contains SQL query only
 * and no parameters.
 */
class StatementQueryOnly implements Statement {
    private static final Object[] parameters = new Object[0];
    private final String sql;

    StatementQueryOnly(@NonNull String sql) {
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
        return Statements.basicHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Statement && Statements.basicallyEquals(this, (Statement) obj);
    }

    @Override
    public String toString() {
        return this.sql;
    }
}
