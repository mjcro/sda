package io.github.mjcro.sda.reflection;

import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * Reads {@code short}/{@link Short} columns. Throws {@link ScalarNullException} when SQL NULL
 * is read into the unboxed variant.
 */
public class ShortTypeHandler extends AbstractSimpleColumnTypeHandler {
    private final boolean boxed;

    /**
     * @param boxed True for {@link Short}, false for {@code short}.
     */
    public ShortTypeHandler(boolean boxed) {
        this.boxed = boxed;
    }

    @Override
    protected boolean supports(@NonNull Class<?> clazz) {
        return boxed
                ? Objects.equals(Short.class, clazz)
                : Objects.equals(short.class, clazz);
    }

    @Override
    protected ValueReader getValueReader(@NonNull String column) {
        return rs -> {
            short value = rs.getShort(column);
            boolean wasNull = rs.wasNull();
            if (wasNull && !boxed) {
                throw new ScalarNullException("short");
            }
            return wasNull ? null : value;
        };
    }
}
