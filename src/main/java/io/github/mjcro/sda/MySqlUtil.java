package io.github.mjcro.sda;

import io.github.mjcro.interfaces.database.Statement;
import io.github.mjcro.writers.sql.MySqlNameWriter;
import org.jspecify.annotations.NonNull;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/**
 * Contains various MySQL-related utilities.
 */
public class MySqlUtil {
    private MySqlUtil() {
    }

    public static void writeEqOrIn(@NonNull StringBuilder sb, @NonNull Collection<?> collection) {
        if (collection.size() == 1) {
            sb.append("=?");
        } else {
            sb.append(" IN (");
            PlaceholdersWriter.QUESTIONS.writeTo(sb, collection.size());
            sb.append(")");
        }
    }

    /**
     * Performs name escaping (table name, column name, etc.)
     *
     * @param name Source name.
     * @return Escaped name.
     */
    public static @NonNull String name(@NonNull String name) {
        return MySqlNameWriter.INSTANCE.asString(name);
    }

    static @NonNull Statement matchColumn(
            @NonNull String table,
            @NonNull String column,
            @NonNull Collection<Object> identifiers
    ) {
        Objects.requireNonNull(table, "table");
        Objects.requireNonNull(column, "column");
        Objects.requireNonNull(identifiers, "identifiers");

        return Statements.build((sb, ph) -> {
            sb.append("SELECT * FROM ");
            MySqlNameWriter.INSTANCE.writeTo(sb, table);
            sb.append(" WHERE ");
            MySqlNameWriter.INSTANCE.writeTo(sb, column);
            writeEqOrIn(sb, identifiers);
            ph.addAll(identifiers);
        });
    }

    static @NonNull Statement matchColumn(
            @NonNull String table,
            @NonNull String columnOne,
            @NonNull Collection<Object> identifiersOne,
            @NonNull String columnTwo,
            @NonNull Collection<Object> identifiersTwo
    ) {
        Objects.requireNonNull(table, "table");
        Objects.requireNonNull(columnOne, "columnOne");
        Objects.requireNonNull(columnTwo, "columnTwo");
        Objects.requireNonNull(identifiersOne, "identifiersOne");
        Objects.requireNonNull(identifiersTwo, "identifiersTwo");

        return Statements.build((sb, ph) -> {
            sb.append("SELECT * FROM ");
            MySqlNameWriter.INSTANCE.writeTo(sb, table);
            sb.append(" WHERE ");

            MySqlNameWriter.INSTANCE.writeTo(sb, columnOne);
            writeEqOrIn(sb, identifiersOne);

            sb.append(" AND ");
            MySqlNameWriter.INSTANCE.writeTo(sb, columnTwo);
            writeEqOrIn(sb, identifiersTwo);
            ph.addAll(identifiersOne);
            ph.addAll(identifiersTwo);
        });
    }

    static @NonNull Statement insert(
            @NonNull String table,
            @NonNull Map<String, Object> values
    ) {
        Objects.requireNonNull(table, "table");
        Objects.requireNonNull(values, "values");

        return Statements.build((sb, ph) -> {
            sb.append("INSERT INTO ");
            MySqlNameWriter.INSTANCE.writeTo(sb, table);
            sb.append(" (");
            boolean first = true;
            for (Map.Entry<String, Object> entry : values.entrySet()) {
                if (first) {
                    first = false;
                } else {
                    sb.append(",");
                }
                MySqlNameWriter.INSTANCE.writeTo(sb, entry.getKey());
                ph.add(entry.getValue());
            }
            sb.append(") VALUES (");
            PlaceholdersWriter.QUESTIONS.writeTo(sb, values.size());
            sb.append(")");
        });
    }

    static @NonNull Statement updateByColumn(
            @NonNull String table,
            @NonNull String columnName,
            @NonNull Object columnValue,
            @NonNull Map<String, Object> values
    ) {
        Objects.requireNonNull(table, "table");
        Objects.requireNonNull(columnName, "columnName");
        Objects.requireNonNull(values, "values");

        return Statements.build((sb, ph) -> {
            sb.append("UPDATE ");
            MySqlNameWriter.INSTANCE.writeTo(sb, table);
            sb.append(" SET ");
            int i = 0;
            for (Map.Entry<String, Object> e : values.entrySet()) {
                if (i++ > 0) {
                    sb.append(",");
                }
                MySqlNameWriter.INSTANCE.writeTo(sb, e.getKey());
                sb.append("=?");
                ph.add(e.getValue());
            }
            sb.append(" WHERE ");
            MySqlNameWriter.INSTANCE.writeTo(sb, columnName);
            sb.append("=?");
            ph.add(columnValue);
        });
    }

    static @NonNull Statement deleteByColumn(
            @NonNull String table,
            @NonNull String columnName,
            @NonNull Object columnValue
    ) {
        Objects.requireNonNull(table, "table");
        Objects.requireNonNull(columnName, "columnName");

        return Statements.build((sb, ph) -> {
            sb.append("DELETE FROM ");
            MySqlNameWriter.INSTANCE.writeTo(sb, table);
            sb.append(" WHERE ");
            MySqlNameWriter.INSTANCE.writeTo(sb, columnName);
            sb.append("=?");
            ph.add(columnValue);
        });
    }
}
