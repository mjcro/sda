package io.github.mjcro.sda.reflection;

import org.jspecify.annotations.NonNull;

import java.time.Duration;
import java.util.Objects;

public class DurationMillisTypeHandler extends AbstractSimpleColumnTypeHandler {
    @Override
    protected boolean supports(@NonNull Class<?> clazz) {
        return Objects.equals(Duration.class, clazz);
    }

    @Override
    protected ValueReader getValueReader(@NonNull String column) {
        return rs -> {
            long value = rs.getLong(column);
            return rs.wasNull() ? null : Duration.ofMillis(value);
        };
    }
}
