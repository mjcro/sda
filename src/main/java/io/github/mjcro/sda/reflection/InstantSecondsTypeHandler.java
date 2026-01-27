package io.github.mjcro.sda.reflection;

import org.jspecify.annotations.NonNull;

import java.time.Instant;
import java.util.Objects;

public class InstantSecondsTypeHandler extends AbstractSimpleColumnTypeHandler {
    @Override
    protected boolean supports(@NonNull Class<?> clazz) {
        return Objects.equals(Instant.class, clazz);
    }

    @Override
    protected ValueReader getValueReader(@NonNull String column) {
        return rs -> {
            long value = rs.getLong(column);
            return rs.wasNull() ? null : Instant.ofEpochSecond(value);
        };
    }
}
