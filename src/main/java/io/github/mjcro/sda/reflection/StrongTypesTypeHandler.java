package io.github.mjcro.sda.reflection;

import io.github.mjcro.interfaces.StrongType;
import io.github.mjcro.interfaces.ints.StrongInt;
import io.github.mjcro.interfaces.longs.StrongLong;
import io.github.mjcro.sda.UnsupportedAnnotatedElementException;
import io.github.mjcro.sda.VirtualColumn;
import org.jspecify.annotations.NonNull;

import java.lang.reflect.Constructor;

public class StrongTypesTypeHandler extends AbstractColumnTypeHandler {
    @Override
    protected boolean supports(@NonNull Class<?> clazz) {
        if (StrongLong.class.isAssignableFrom(clazz) || StrongInt.class.isAssignableFrom(clazz)) {
            return true;
        } else if (StrongType.class.isAssignableFrom(clazz)) {
            try {
                return clazz.getMethod("value").getReturnType() == String.class;
            } catch (NoSuchMethodException e) {
                return false;
            }
        }
        return false;
    }

    @Override
    protected @NonNull ValueReader getValueReader(@NonNull String column, @NonNull Class<?> clazz) {
        try {
            if (StrongType.class.isAssignableFrom(clazz)) {
                // Only strings supported
                Constructor<?> ctor = clazz.getDeclaredConstructor(String.class);
                ctor.setAccessible(true);
                return rs -> {
                    String value = rs.getString(column);
                    return value == null
                            ? null
                            : ctor.newInstance(value);
                };
            } else if (StrongLong.class.isAssignableFrom(clazz)) {
                Constructor<?> ctor = clazz.getDeclaredConstructor(long.class);
                ctor.setAccessible(true);
                return rs -> {
                    long value = rs.getLong(column);
                    return rs.wasNull()
                            ? null
                            : ctor.newInstance(value);
                };
            } else if (StrongInt.class.isAssignableFrom(clazz)) {
                Constructor<?> ctor = clazz.getDeclaredConstructor(int.class);
                ctor.setAccessible(true);
                return rs -> {
                    int value = rs.getInt(column);
                    return rs.wasNull()
                            ? null
                            : ctor.newInstance(value);
                };
            }
            throw new ReflectiveOperationException("Unsupported type " + clazz);
        } catch (ReflectiveOperationException e) {
            throw new UnsupportedAnnotatedElementException(this.getClass(), new VirtualColumn<>(column, clazz));
        }
    }
}
