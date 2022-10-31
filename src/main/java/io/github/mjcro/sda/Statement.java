package io.github.mjcro.sda;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.function.BiConsumer;

public interface Statement extends StatementPrototype {
    /**
     * Constructs new statement instance for given query and placeholders.
     *
     * @param sql          Query.
     * @param placeholders Placeholders.
     * @return Statement.
     */
    static Statement of(String sql, Object[] placeholders) {
        return placeholders == null || placeholders.length == 0
                ? new StatementQueryOnly(sql)
                : new StatementSimple(sql, placeholders);
    }

    /**
     * Constructs statement instance using provided builder function.
     *
     * @param builder Builder function.
     * @return Statement.
     */
    static Statement build(BiConsumer<StringBuilder, ArrayList<Object>> builder) {
        StringBuilder stringBuilder = new StringBuilder();
        ArrayList<Object> placeholders = new ArrayList<>();
        builder.accept(stringBuilder, placeholders);
        return of(stringBuilder.toString(), placeholders.toArray());
    }

    /**
     * Constructs statement prototype containing matcher by column values.
     *
     * @param table       Table to match data from.
     * @param column      Column to match.
     * @param identifiers Column identifiers.
     * @return Statement prototype.
     */
    static StatementPrototype matchColumn(
            String table,
            String column,
            Collection<Object> identifiers
    ) {
        return dialect -> {
            if (dialect.isCompatibleWith(Dialect.MySQL)) {
                return MySqlUtil.matchColumn(table, column, identifiers);
            }
            throw new UnsupportedDialectException(dialect);
        };
    }

    /**
     * Constructs statement prototype containing matcher by column values.
     *
     * @param table          Table to match data from.
     * @param columnOne      Column to match.
     * @param identifiersOne Column identifiers.
     * @param columnTwo      Column to match.
     * @param identifiersTwo Column identifiers.
     * @return Statement prototype.
     */
    static StatementPrototype matchColumn(
            String table,
            String columnOne,
            Collection<Object> identifiersOne,
            String columnTwo,
            Collection<Object> identifiersTwo
    ) {
        return dialect -> {
            if (dialect.isCompatibleWith(Dialect.MySQL)) {
                return MySqlUtil.matchColumn(table, columnOne, identifiersOne, columnTwo, identifiersTwo);
            }
            throw new UnsupportedDialectException(dialect);
        };
    }

    /**
     * Constructs statement prototype containing INSERT statement.
     *
     * @param table  Table to insert data into.
     * @param values Values to insert.
     * @return Statement prototype.
     */
    static StatementPrototype insert(
            String table,
            Map<String, Object> values
    ) {
        return dialect -> {
            if (dialect.isCompatibleWith(Dialect.MySQL)) {
                return MySqlUtil.insert(table, values);
            }
            throw new UnsupportedDialectException(dialect);
        };
    }

    /**
     * Returns true if both statements has same query and placeholders.
     *
     * @param a First statement.
     * @param b Second statement.
     * @return True if statements has same query and placeholder.
     */
    static boolean basicallyEquals(Statement a, Statement b) {
        if (a == null || b == null) {
            return false; // If any of statements is null they are not equal.
        }

        if (!a.getSql().equals(b.getSql())) {
            return false; // SQL differs
        }

        return Arrays.equals(a.getPlaceholders(), b.getPlaceholders());
    }

    /**
     * Constructs basic (standard) hash code for given statement.
     *
     * @param s Statement to calculate hash for.
     * @return Calculated hash code.
     */
    static int basicHashCode(Statement s) {
        return s == null ? 0 : s.getSql().hashCode();
    }

    @Override
    default Statement createStatement(Dialect dialect) {
        return this;
    }

    /**
     * @return Statement query.
     */
    String getSql();

    /**
     * @return Statement placeholders.
     */
    Object[] getPlaceholders();
}
