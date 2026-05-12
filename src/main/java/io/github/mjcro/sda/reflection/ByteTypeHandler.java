package io.github.mjcro.sda.reflection;

import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * Reads {@code byte}/{@link Byte} columns. Throws {@link ScalarNullException} when SQL NULL
 * is read into the unboxed variant.
 */
public class ByteTypeHandler extends AbstractSimpleColumnTypeHandler {
    private final boolean boxed;

    /**
     * @param boxed True for {@link Byte}, false for {@code byte}.
     */
    public ByteTypeHandler(boolean boxed) {
        this.boxed = boxed;
    }

    @Override
    protected boolean supports(@NonNull Class<?> clazz) {
        return boxed
                ? Objects.equals(Byte.class, clazz)
                : Objects.equals(byte.class, clazz);
    }

    @Override
    protected ValueReader getValueReader(@NonNull String column) {
        return rs -> {
            byte value = rs.getByte(column);
            boolean wasNull = rs.wasNull();
            if (wasNull && !boxed) {
                throw new ScalarNullException("byte");
            }
            return wasNull ? null : value;
        };
    }
}
