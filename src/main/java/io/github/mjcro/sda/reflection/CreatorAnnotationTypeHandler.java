package io.github.mjcro.sda.reflection;

import io.github.mjcro.sda.Creator;
import io.github.mjcro.sda.TypeHandler;
import io.github.mjcro.sda.UnsupportedAnnotatedElementException;
import io.github.mjcro.sda.VirtualColumn;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class CreatorAnnotationTypeHandler extends AbstractColumnTypeHandler {
    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (Modifier.isStatic(method.getModifiers()) && method.getParameterCount() == 1 && method.isAnnotationPresent(Creator.class)) {
                Class<?> parameterType = method.getParameters()[0].getType();
                return getSubHandler(parameterType) != null;
            }
        }
        for (Constructor<?> ctor : clazz.getDeclaredConstructors()) {
            if (ctor.getParameterCount() == 1 && ctor.isAnnotationPresent(Creator.class)) {
                Class<?> parameterType = ctor.getParameters()[0].getType();
                return getSubHandler(parameterType) != null;
            }
        }
        return false;
    }

    @Override
    public ValueReader getValueReader(@NonNull String column, @NonNull Class<?> clazz) throws UnsupportedAnnotatedElementException {
        for (Method method : clazz.getDeclaredMethods()) {
            if (Modifier.isStatic(method.getModifiers()) && method.getParameterCount() == 1 && method.isAnnotationPresent(Creator.class)) {
                Class<?> parameterType = method.getParameters()[0].getType();
                TypeHandler subHandler = getSubHandler(parameterType);
                if (subHandler != null) {
                    ValueReader subReader = subHandler.getValueReader(new VirtualColumn<>(column, parameterType));
                    method.setAccessible(true);
                    return rs -> {
                        Object value = subReader.getValue(rs);
                        return value == null ? null : method.invoke(null, value);
                    };
                }
            }
        }
        for (Constructor<?> ctor : clazz.getDeclaredConstructors()) {
            if (ctor.getParameterCount() == 1 && ctor.isAnnotationPresent(Creator.class)) {
                Class<?> parameterType = ctor.getParameters()[0].getType();
                TypeHandler subHandler = getSubHandler(parameterType);
                if (subHandler != null) {
                    ValueReader subReader = subHandler.getValueReader(new VirtualColumn<>(column, parameterType));
                    ctor.setAccessible(true);
                    return rs -> {
                        Object value = subReader.getValue(rs);
                        return value == null ? null : ctor.newInstance(value);
                    };
                }
            }
        }

        throw new UnsupportedAnnotatedElementException(this.getClass(), new VirtualColumn<>(column, clazz));
    }

    private static @Nullable TypeHandler getSubHandler(@NonNull Class<?> parameterType) {
        if (parameterType == String.class) {
            return new StringTypeHandler();
        } else if (parameterType == int.class) {
            return new IntTypeHandler(false);
        } else if (parameterType == Integer.class) {
            return new IntTypeHandler(true);
        } else if (parameterType == long.class) {
            return new LongTypeHandler(false);
        } else if (parameterType == Long.class) {
            return new LongTypeHandler(false);
        }
        return null;
    }
}
