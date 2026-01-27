package io.github.mjcro.sda.reflection;

import org.jspecify.annotations.NonNull;

import java.util.Objects;

public class IntTypeHandler extends AbstractSimpleColumnTypeHandler {
    private final boolean boxed;

    public IntTypeHandler(boolean boxed) {
        this.boxed = boxed;
    }

    @Override
    protected boolean supports(@NonNull Class<?> clazz) {
        return boxed
                ? Objects.equals(Integer.class, clazz)
                : Objects.equals(int.class, clazz);
    }

    @Override
    protected ValueReader getValueReader(@NonNull String column) {
        return rs -> {
            int value = rs.getInt(column);
            boolean wasNull = rs.wasNull();
            if (wasNull && !boxed) {
                throw new ScalarNullException("int");
            }
            return wasNull ? null : value;
        };
    }
}
