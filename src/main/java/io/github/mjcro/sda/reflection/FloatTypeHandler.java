package io.github.mjcro.sda.reflection;

import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * Reads {@code float}/{@link Float} columns. Throws {@link ScalarNullException} when SQL NULL
 * is read into the unboxed variant.
 */
public class FloatTypeHandler extends AbstractSimpleColumnTypeHandler {
    private final boolean boxed;

    /**
     * @param boxed True for {@link Float}, false for {@code float}.
     */
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
