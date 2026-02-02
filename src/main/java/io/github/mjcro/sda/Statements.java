package io.github.mjcro.sda;

import io.github.mjcro.interfaces.database.Statement;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.function.BiConsumer;

public class Statements {
    /**
     * Constructs new statement instance for given query and parameters.
     *
     * @param sql        Query.
     * @param parameters Parameters.
     * @return Statement.
     */
    public static @NonNull Statement of(@NonNull String sql, @Nullable Object @Nullable [] parameters) {
        return parameters == null || parameters.length == 0
                ? new StatementQueryOnly(sql)
                : new StatementSimple(sql, parameters);
    }

    /**
     * Constructs statement instance using provided builder function.
     *
     * @param builder Builder function.
     * @return Statement.
     */
    public static @NonNull Statement build(@NonNull BiConsumer<StringBuilder, ArrayList<Object>> builder) {
        StringBuilder stringBuilder = new StringBuilder();
        ArrayList<Object> parameters = new ArrayList<>();
        builder.accept(stringBuilder, parameters);
        return of(stringBuilder.toString(), parameters.toArray());
    }

    /**
     * Constructs statement prototype containing matcher by column values.
     *
     * @param table       Table to match data from.
     * @param column      Column to match.
     * @param identifiers Column identifiers.
     * @return Statement prototype.
     */
    public static @NonNull StatementPrototype matchColumn(
            @NonNull String table,
            @NonNull String column,
            @NonNull Collection<Object> identifiers
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
    public static @NonNull StatementPrototype matchColumn(
            @NonNull String table,
            @NonNull String columnOne,
            @NonNull Collection<Object> identifiersOne,
            @NonNull String columnTwo,
            @NonNull Collection<Object> identifiersTwo
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
    public static @NonNull StatementPrototype insert(
            @NonNull String table,
            @NonNull Map<String, Object> values
    ) {
        return dialect -> {
            if (dialect.isCompatibleWith(Dialect.MySQL)) {
                return MySqlUtil.insert(table, values);
            }
            throw new UnsupportedDialectException(dialect);
        };
    }

    /**
     * Constructs statement prototype containing UPDATE ... WHERE x=? statement.
     *
     * @param table       Table to update data in.
     * @param columnName  Column name.
     * @param columnValue Column value.
     * @param values      Values to update.
     * @return Statement prototype.
     */
    public static @NonNull StatementPrototype updateByColumn(
            @NonNull String table,
            @NonNull String columnName,
            @NonNull Object columnValue,
            @NonNull Map<String, Object> values
    ) {
        return dialect -> {
            if (dialect.isCompatibleWith(Dialect.MySQL)) {
                return MySqlUtil.updateByColumn(table, columnName, columnValue, values);
            }
            throw new UnsupportedDialectException(dialect);
        };
    }

    /**
     * Constructs statement prototype containing DELETE ... WHERE x=? statement.
     *
     * @param table       Table to update data in.
     * @param columnName  Column name.
     * @param columnValue Column value.
     * @return Statement prototype.
     */
    public static @NonNull StatementPrototype deleteByColumn(
            @NonNull String table,
            @NonNull String columnName,
            @NonNull Object columnValue
    ) {
        return dialect -> {
            if (dialect.isCompatibleWith(Dialect.MySQL)) {
                return MySqlUtil.deleteByColumn(table, columnName, columnValue);
            }
            throw new UnsupportedDialectException(dialect);
        };
    }

    /**
     * Returns true if both statements has same query and parameters.
     *
     * @param a First statement.
     * @param b Second statement.
     * @return True if statements has same query and parameters.
     */
    public static boolean basicallyEquals(@Nullable Statement a, @Nullable Statement b) {
        if (a == null || b == null) {
            return false; // If any of statements is null they are not equal.
        }

        if (!a.getSql().equals(b.getSql())) {
            return false; // SQL differs
        }

        return Arrays.equals(a.getParameters(), b.getParameters());
    }

    /**
     * Constructs basic (standard) hash code for given statement.
     *
     * @param s Statement to calculate hash for.
     * @return Calculated hash code.
     */
    public static int basicHashCode(@Nullable Statement s) {
        return s == null ? 0 : s.getSql().hashCode();
    }

    private Statements() {
    }
}
