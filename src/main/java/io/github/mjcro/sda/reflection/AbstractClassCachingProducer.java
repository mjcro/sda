package io.github.mjcro.sda.reflection;

import io.github.mjcro.sda.FieldWriter;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

abstract class AbstractClassCachingProducer implements FieldWriterProducer {
    private final ConcurrentHashMap<Class<?>, FieldWriterProducer> cache = new ConcurrentHashMap<>();

    public abstract boolean isSupported(Class<?> clazz);

    public abstract FieldWriterProducer initializeCache(Class<?> clazz);

    @Override
    public final Optional<FieldWriter<?>> apply(final Field field, final String columnName) {
        Class<?> fieldType = field.getType();
        if (cache.containsKey(fieldType) || isSupported(fieldType)) {
            return cache.computeIfAbsent(fieldType, this::initializeCache).apply(field, columnName);
        }
        return Optional.empty();
    }
}
