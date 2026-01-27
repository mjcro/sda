package io.github.mjcro.sda.reflection;

import org.jspecify.annotations.NonNull;

import java.util.Objects;

public class StringBooleanTypeHandler extends AbstractSimpleColumnTypeHandler {
    private final boolean boxed;

    public StringBooleanTypeHandler(boolean boxed) {
        this.boxed = boxed;
    }

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
