package io.github.mjcro.sda.reflection;

import org.jspecify.annotations.NonNull;

import java.util.Objects;

public class DoubleTypeHandler extends AbstractSimpleColumnTypeHandler {
    private final boolean boxed;

    public DoubleTypeHandler(boolean boxed) {
        this.boxed = boxed;
    }

    @Override
    protected boolean supports(@NonNull Class<?> clazz) {
        return boxed
                ? Objects.equals(Double.class, clazz)
                : Objects.equals(double.class, clazz);
    }

    @Override
    protected ValueReader getValueReader(@NonNull String column) {
        return rs -> {
            double value = rs.getDouble(column);
            boolean wasNull = rs.wasNull();
            if (wasNull && !boxed) {
                throw new ScalarNullException("double");
            }
            return wasNull ? null : value;
        };
    }
}
