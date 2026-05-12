package io.github.mjcro.sda.reflection;

import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * Reads {@code double}/{@link Double} columns. Throws {@link ScalarNullException} when SQL NULL
 * is read into the unboxed variant.
 */
public class DoubleTypeHandler extends AbstractSimpleColumnTypeHandler {
    private final boolean boxed;

    /**
     * @param boxed True for {@link Double}, false for {@code double}.
     */
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
