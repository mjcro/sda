package io.github.mjcro.sda;

import org.jspecify.annotations.NonNull;

/**
 * Provides {@link RowMapper} instances for given target classes.
 */
public interface RowMapperFactory {
    /**
     * Returns a row mapper able to produce instances of the requested class.
     *
     * @param clazz Target class, not null.
     * @param <T>   Target type.
     * @return Row mapper, not null.
     * @throws RowMapperException If no mapper can be resolved for the class.
     */
    @NonNull <T> RowMapper<T> get(@NonNull Class<T> clazz);
}
