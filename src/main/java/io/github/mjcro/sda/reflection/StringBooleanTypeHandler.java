package io.github.mjcro.sda.reflection;

import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * Reads a boolean from a string column. Recognized truthy values (case-insensitive):
 * {@code true}, {@code enabled}, {@code enable}, {@code yes}, {@code on}.
 * Anything else is treated as false. SQL NULL maps to null for {@link Boolean}
 * or raises {@link ScalarNullException} for unboxed {@code boolean}.
 */
public class StringBooleanTypeHandler extends AbstractSimpleColumnTypeHandler {
    private final boolean boxed;

    /**
     * @param boxed True for {@link Boolean}, false for {@code boolean}.
     */
    public StringBooleanTypeHandler(boolean boxed) {
        this.boxed = boxed;
    }

    /**
     * @param clazz Either {@code boolean.class} or {@code Boolean.class}. Not null.
     * @throws IllegalArgumentException If the class is neither.
     */
    public StringBooleanTypeHandler(@NonNull Class<?> clazz) {
        this(detectBoxingFronClass(clazz));
    }

    private static boolean detectBoxingFronClass(@NonNull Class<?> clazz) {
        if (clazz == boolean.class) {
            return false;
        } else if (clazz == Boolean.class) {
            return true;
        } else {
            throw new IllegalArgumentException("Unsupported class " + clazz);
        }
    }

    @Override
    protected boolean supports(@NonNull Class<?> clazz) {
        return boxed
                ? Objects.equals(Boolean.class, clazz)
                : Objects.equals(boolean.class, clazz);
    }

    /**
     * @param value String to interpret. Not null.
     * @return True if the value is one of the recognized truthy literals.
     */
    public boolean isTrue(@NonNull String value) {
        return "true".equalsIgnoreCase(value)
                || "enabled".equalsIgnoreCase(value)
                || "enable".equalsIgnoreCase(value)
                || "yes".equalsIgnoreCase(value)
                || "on".equalsIgnoreCase(value);
    }

    @Override
    protected ValueReader getValueReader(@NonNull String column) {
        return rs -> {
            String value = rs.getString(column);
            boolean wasNull = rs.wasNull();
            if (wasNull && !boxed) {
                throw new ScalarNullException("bool");
            }
            return rs.wasNull() ? null : isTrue(value);
        };
    }
}
