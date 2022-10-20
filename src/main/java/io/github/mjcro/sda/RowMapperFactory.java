package io.github.mjcro.sda;

public interface RowMapperFactory {
    <T> RowMapper<T> get(final Class<T> clazz);
}
