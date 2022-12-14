package io.github.mjcro.sda.reflection;

import io.github.mjcro.sda.Creator;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Optional;

public class FieldWriterProducerByCreatorAnnotation extends AbstractClassCachingProducer {
    @Override
    public boolean isSupported(Class<?> clazz) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (Modifier.isStatic(method.getModifiers()) && method.isAnnotationPresent(Creator.class)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public FieldWriterProducer initializeCache(Class<?> clazz) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (Modifier.isStatic(method.getModifiers()) && method.isAnnotationPresent(Creator.class)) {
                if (method.getParameterCount() != 1) {
                    throw new RuntimeException("Expected 1 argument");
                }
                method.setAccessible(true);
                Class<?> parameterType = method.getParameterTypes()[0];
                if (parameterType == String.class) {
                    return (field, columnName) -> Optional.of((FieldWriterReflective<Object>) (rs, to) -> field.set(to, method.invoke(null, rs.getString(columnName))));
                } else if (parameterType == int.class) {
                    return (field, columnName) -> Optional.of((FieldWriterReflective<Object>) (rs, to) -> field.set(to, method.invoke(null, rs.getInt(columnName))));
                } else if (parameterType == long.class) {
                    return (field, columnName) -> Optional.of((FieldWriterReflective<Object>) (rs, to) -> field.set(to, method.invoke(null, rs.getLong(columnName))));
                } else if (parameterType == Long.class) {
                    return (field, columnName) -> Optional.of((FieldWriterReflective<Object>) (rs, to) -> {
                        Long value = rs.getLong(columnName);
                        if (rs.wasNull()) {
                            value = null;
                        }
                        field.set(to, method.invoke(null, value));
                    });
                } else if (parameterType == Integer.class) {
                    return (field, columnName) -> Optional.of((FieldWriterReflective<Object>) (rs, to) -> {
                        Integer value = rs.getInt(columnName);
                        if (rs.wasNull()) {
                            value = null;
                        }
                        field.set(to, method.invoke(null, value));
                    });
                }
                throw new RuntimeException("Unsupported @Creator parameter type " + parameterType);
            }
        }
        throw new RuntimeException("Invalid class");
    }
}
