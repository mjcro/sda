package io.github.mjcro.sda;

import org.jspecify.annotations.NonNull;

public interface RowMapperFactory {
    @NonNull <T> RowMapper<T> get(@NonNull Class<T> clazz);
}
