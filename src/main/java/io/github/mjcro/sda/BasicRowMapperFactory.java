package io.github.mjcro.sda;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Thread-safe registry-based {@link RowMapperFactory} backed by a class-to-mapper map.
 */
public class BasicRowMapperFactory implements RowMapperFactory {
    private final Map<Class<?>, RowMapper<?>> mappers = new ConcurrentHashMap<>();

    /**
     * Checks if a mapper is registered for the given class.
     *
     * @param clazz Class to check, may be null.
     * @return True if a mapper is registered, false otherwise (including null input).
     */
    public boolean contains(@Nullable Class<?> clazz) {
        return clazz != null && mappers.containsKey(clazz);
    }

    /**
     * Registers a row mapper for the given class, replacing any existing mapping.
     *
     * @param clazz  Target class, not null.
     * @param mapper Row mapper instance, not null.
     */
    public void register(@NonNull Class<?> clazz, @NonNull RowMapper<?> mapper) {
        this.mappers.put(
                Objects.requireNonNull(clazz, "clazz"),
                Objects.requireNonNull(mapper, "mapper")
        );
    }

    /**
     * Bulk-registers mappers from the given map. Null or empty input is silently ignored.
     *
     * @param map Class-to-mapper map, may be null.
     */
    public void register(@Nullable Map<Class<?>, RowMapper<?>> map) {
        if (map != null && !map.isEmpty()) {
            for (Map.Entry<Class<?>, RowMapper<?>> entry : map.entrySet()) {
                this.register(entry.getKey(), entry.getValue());
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public @NonNull <T> RowMapper<T> get(@NonNull Class<T> clazz) {
        RowMapper<?> mapper = mappers.get(clazz);
        if (mapper == null) {
            throw new RowMapperException(clazz);
        }
        return (RowMapper<T>) mapper;
    }
}
