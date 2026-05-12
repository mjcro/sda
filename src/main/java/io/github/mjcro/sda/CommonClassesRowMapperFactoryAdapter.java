package io.github.mjcro.sda;

import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * Decorator over a {@link RowMapperFactory} that intercepts requests for common scalar
 * types ({@code long}, {@code int}, {@link String}) and returns mappers that read the
 * first column of the result set directly, delegating everything else to the wrapped factory.
 */
public class CommonClassesRowMapperFactoryAdapter implements RowMapperFactory {
    private final RowMapperFactory inner;

    /**
     * @param inner Delegate factory used for types not handled here. Not null.
     */
    public CommonClassesRowMapperFactoryAdapter(@NonNull RowMapperFactory inner) {
        this.inner = Objects.requireNonNull(inner, "inner");
    }

    @SuppressWarnings("unchecked")
    @Override
    public @NonNull <T> RowMapper<T> get(@NonNull final Class<T> clazz) {
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
