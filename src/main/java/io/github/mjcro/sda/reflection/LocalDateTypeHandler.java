package io.github.mjcro.sda.reflection;

import org.jspecify.annotations.NonNull;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Reads {@link LocalDate} from an ISO-8601 string column (e.g. {@code 2024-01-31}).
 */
public class LocalDateTypeHandler extends AbstractSimpleColumnTypeHandler {
    @Override
    protected boolean supports(@NonNull Class<?> clazz) {
        return Objects.equals(LocalDate.class, clazz);
    }

    @Override
    protected ValueReader getValueReader(@NonNull String column) {
        return rs -> {
            String value = rs.getString(column);
            return value == null
                    ? null
                    : LocalDate.parse(value);
        };
    }
}
