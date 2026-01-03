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

    private static final MySqlNameWriter nameWriter = new MySqlNameWriter();

    /**
     * Writes placeholder marks (?) comma-separated into
     * given string builder.
     *
     * @param sb    String builder to write data into.
     * @param times Repeat count.
     */
    public static void writePlaceholders(@NonNull StringBuilder sb, int times) {
        boolean first = true;
        for (int i = 0; i < times; i++) {
            if (first) {
                first = false;
            } else {
                sb.append(",");
            }
            sb.append("?");
        }
    }

    public static void writeEqOrIn(@NonNull StringBuilder sb, @NonNull Collection<?> collection) {
        if (collection.size() == 1) {
            sb.append(" = ?");
        } else {
            sb.append(" IN (");
            writePlaceholders(sb, collection.size());
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
        return nameWriter.asString(name);
    }

    static void writeNameTo(@NonNull StringBuilder sb, @NonNull String name) {
        nameWriter.writeTo(sb, name);
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
            writeNameTo(sb, table);
            sb.append(" WHERE ");
            writeNameTo(sb, column);
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
            writeNameTo(sb, table);
            sb.append(" WHERE ");

            writeNameTo(sb, columnOne);
            writeEqOrIn(sb, identifiersOne);

            sb.append(" AND ");
            writeNameTo(sb, columnTwo);
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
            writeNameTo(sb, table);
            sb.append(" (");
            boolean first = true;
            for (Map.Entry<String, Object> entry : values.entrySet()) {
                if (first) {
                    first = false;
                } else {
                    sb.append(",");
                }
                writeNameTo(sb, entry.getKey());
                ph.add(entry.getValue());
            }
            sb.append(") VALUES (");
            writePlaceholders(sb, values.size());
            sb.append(")");
        });
    }
}
