package io.github.mjcro.sda.reflection;

import io.github.mjcro.sda.FieldWriter;
import org.jspecify.annotations.NonNull;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

abstract class AbstractClassCachingProducer implements FieldWriterProducer {
    private final ConcurrentHashMap<Class<?>, FieldWriterProducer> cache = new ConcurrentHashMap<>();

    public abstract boolean isSupported(@NonNull Class<?> clazz);

    public abstract @NonNull FieldWriterProducer initializeCache(@NonNull Class<?> clazz);

    @Override
    public final @NonNull Optional<FieldWriter<?>> apply(@NonNull final Field field, @NonNull final String columnName) {
        Class<?> fieldType = field.getType();
        if (cache.containsKey(fieldType) || isSupported(fieldType)) {
            return cache.computeIfAbsent(fieldType, this::initializeCache).apply(field, columnName);
        }
        return Optional.empty();
    }
}
