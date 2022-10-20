package io.github.mjcro.sda;

public interface RowMapperFactory {
    <T> RowMapper<T> get(Class<T> clazz);
}
