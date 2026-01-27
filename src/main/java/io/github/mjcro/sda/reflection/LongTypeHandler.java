package io.github.mjcro.sda.reflection;

import org.jspecify.annotations.NonNull;

import java.util.Objects;

public class LongTypeHandler extends AbstractSimpleColumnTypeHandler {
    private final boolean boxed;

    public LongTypeHandler(boolean boxed) {
        this.boxed = boxed;
    }

    @Override
    protected boolean supports(@NonNull Class<?> clazz) {
        return boxed
                ? Objects.equals(Long.class, clazz)
                : Objects.equals(long.class, clazz);
    }

    @Override
    protected ValueReader getValueReader(@NonNull String column) {
        return rs -> {
            long value = rs.getLong(column);
            boolean wasNull = rs.wasNull();
            if (wasNull && !boxed) {
                throw new ScalarNullException("long");
            }
            return wasNull ? null : value;
        };
    }
}
