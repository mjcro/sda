package io.github.mjcro.sda.reflection;

import io.github.mjcro.sda.RowMapper;
import io.github.mjcro.sda.RowMapperException;
import io.github.mjcro.sda.RowMapperFactory;
import io.github.mjcro.sda.TieredTypeHandlerList;
import io.github.mjcro.sda.TypeHandler;
import io.github.mjcro.sda.UnsupportedAnnotatedElementException;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class TypeHandlerRowMapperFactory implements RowMapperFactory {
    private final TieredTypeHandlerList handlerList;
    private final ConcurrentHashMap<Class<?>, RowMapper<?>> rowMappers = new ConcurrentHashMap<>();

    public TypeHandlerRowMapperFactory(@NonNull TieredTypeHandlerList handlerList) {
        this.handlerList = Objects.requireNonNull(handlerList, "handlerList");
    }

    @SuppressWarnings("unchecked")
    @Override
    public @NonNull <T> RowMapper<T> get(@NonNull Class<T> clazz) {
        return (RowMapper<T>) rowMappers.computeIfAbsent(clazz, this::produce);
    }

    private @NonNull RowMapper<?> produce(@NonNull Class<?> clazz) {
        if (!handlerList.supports(clazz)) {
            throw new RowMapperException(
                    clazz,
                    UnsupportedAnnotatedElementException.forExplainer(handlerList, clazz)
            );
        }
        TypeHandler.ValueReader reader = handlerList.getValueReader(clazz);
        return new TypeHandlerRowMapper(clazz, reader);
    }

    private static class TypeHandlerRowMapper implements RowMapper<Object> {
        private final Class<?> clazz;
        private final TypeHandler.ValueReader reader;

        private TypeHandlerRowMapper(@NonNull Class<?> clazz, TypeHandler.@NonNull ValueReader reader) {
            this.clazz = clazz;
            this.reader = reader;
        }

        @Override
        public @Nullable Object mapRow(@NonNull ResultSet rs) throws SQLException {
            try {
                return reader.getValue(rs);
            } catch (SQLException e) {
                throw e;
            } catch (Exception e) {
                throw new SQLException(
                        "Got ValueReader exception while processing class " + clazz.getName(),
                        e
                );
            }
        }
    }
}
