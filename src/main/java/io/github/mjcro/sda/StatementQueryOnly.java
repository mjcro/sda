package io.github.mjcro.sda;

import java.util.Objects;

class StatementQueryOnly implements Statement {
    private static final Object[] placeholders = new Object[0];
    private final String sql;

    StatementQueryOnly(String sql) {
        this.sql = Objects.requireNonNull(sql, "sql");
    }

    @Override
    public String getSql() {
        return sql;
    }

    @Override
    public Object[] getPlaceholders() {
        return placeholders;
    }
}
