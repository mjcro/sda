package io.github.mjcro.sda;

import java.util.Objects;

public class CommonClassesRowMapperFactoryAdapter implements RowMapperFactory {
    private final RowMapperFactory inner;

    public CommonClassesRowMapperFactoryAdapter(RowMapperFactory inner) {
        this.inner = Objects.requireNonNull(inner, "inner");
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> RowMapper<T> get(final Class<T> clazz) {
        if (clazz == long.class) {
            return rs -> (T) Long.valueOf(rs.getLong(1));
        } else if (clazz == int.class) {
            return rs -> (T) Integer.valueOf(rs.getInt(1));
        } else if (clazz == String.class) {
            return rs -> (T) rs.getObject(1);
        } else {
            return inner.get(clazz);
        }
    }
}
