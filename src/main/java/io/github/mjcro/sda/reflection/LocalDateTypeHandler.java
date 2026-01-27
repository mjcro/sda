package io.github.mjcro.sda.reflection;

import org.jspecify.annotations.NonNull;

import java.time.LocalDate;
import java.util.Objects;

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
