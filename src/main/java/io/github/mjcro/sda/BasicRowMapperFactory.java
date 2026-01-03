package io.github.mjcro.sda;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class BasicRowMapperFactory implements RowMapperFactory {
    private final Map<Class<?>, RowMapper<?>> mappers = new ConcurrentHashMap<>();

    public boolean contains(@Nullable Class<?> clazz) {
        return clazz != null && mappers.containsKey(clazz);
    }

    public void register(@NonNull Class<?> clazz, @NonNull RowMapper<?> mapper) {
        this.mappers.put(
                Objects.requireNonNull(clazz, "clazz"),
                Objects.requireNonNull(mapper, "mapper")
        );
    }

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
