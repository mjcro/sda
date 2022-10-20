package io.github.mjcro.sda;

import java.util.Objects;

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
}
