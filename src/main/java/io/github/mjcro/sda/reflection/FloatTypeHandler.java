package io.github.mjcro.sda.reflection;

import org.jspecify.annotations.NonNull;

import java.util.Objects;

public class FloatTypeHandler extends AbstractSimpleColumnTypeHandler {
    private final boolean boxed;

    public FloatTypeHandler(boolean boxed) {
        this.boxed = boxed;
    }

    @Override
    protected boolean supports(@NonNull Class<?> clazz) {
        return boxed
                ? Objects.equals(Float.class, clazz)
                : Objects.equals(float.class, clazz);
    }

    @Override
    protected ValueReader getValueReader(@NonNull String column) {
        return rs -> {
            float value = rs.getFloat(column);
            boolean wasNull = rs.wasNull();
            if (wasNull && !boxed) {
                throw new ScalarNullException("float");
            }
            return wasNull ? null : value;
        };
    }
}
