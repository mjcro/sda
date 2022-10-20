package io.github.mjcro.sda;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/**
 * Contains various MySQL-related utilities.
 */
public class MySqlUtil {
    private MySqlUtil() {
    }

    /**
     * Writes placeholder marks (?) comma-separated into
     * given string builder.
     *
     * @param sb    String builder to write data into.
     * @param times Repeat count.
     */
    public static void writePlaceholders(StringBuilder sb, int times) {
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

    public static void writeEqOrIn(StringBuilder sb, Collection<?> collection) {
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
    public static String name(String name) {
        if (name.startsWith("`") && name.endsWith("`")) {
            return name;
        }

        return "`" + name + "`";
    }

    static boolean isCompatible(Dialect dialect) {
        return dialect == Dialect.MySQL || dialect == Dialect.H2;
    }

    static Statement matchColumn(
            String table,
            String column,
            Collection<Object> identifiers
    ) {
        Objects.requireNonNull(table, "table");
        Objects.requireNonNull(column, "column");
        Objects.requireNonNull(identifiers, "identifiers");

        return Statement.build((sb, ph) -> {
            sb.append("SELECT * FROM ").append(name(table)).append(" WHERE ").append(name(column));
            writeEqOrIn(sb, identifiers);
            ph.addAll(identifiers);
        });
    }

    static Statement matchColumn(
            String table,
            String columnOne,
            Collection<Object> identifiersOne,
            String columnTwo,
            Collection<Object> identifiersTwo
    ) {
        Objects.requireNonNull(table, "table");
        Objects.requireNonNull(columnOne, "columnOne");
        Objects.requireNonNull(columnTwo, "columnTwo");
        Objects.requireNonNull(identifiersOne, "identifiersOne");
        Objects.requireNonNull(identifiersTwo, "identifiersTwo");

        return Statement.build((sb, ph) -> {
            sb.append("SELECT * FROM ").append(name(table)).append(" WHERE ");

            sb.append(name(columnOne));
            writeEqOrIn(sb, identifiersOne);

            sb.append(" AND ").append(name(columnTwo));
            writeEqOrIn(sb, identifiersTwo);
            ph.addAll(identifiersOne);
            ph.addAll(identifiersTwo);
        });
    }

    static Statement insert(
            String table,
            Map<String, Object> values
    ) {
        Objects.requireNonNull(table, "table");
        Objects.requireNonNull(values, "values");

        return Statement.build((sb, ph) -> {
            sb.append("INSERT INTO ").append(name(table)).append(" (");
            boolean first = true;
            for (Map.Entry<String, Object> entry : values.entrySet()) {
                if (first) {
                    first = false;
                } else {
                    sb.append(",");
                }
                sb.append(name(entry.getKey()));
                ph.add(entry.getValue());
            }
            sb.append(") VALUES (");
            writePlaceholders(sb, values.size());
            sb.append(")");
        });
    }
}
