package io.github.mjcro.sda.reflection;

import io.github.mjcro.sda.RowMapper;
import io.github.mjcro.sda.RowMapperFactory;
import io.github.mjcro.sda.TieredTypeHandlerList;
import io.github.mjcro.sda.TypeHandler;
import org.jspecify.annotations.NonNull;

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
            // TODO throw, maybe with explanation
        }
        TypeHandler.ValueReader reader = handlerList.getValueReader(clazz);
        return (RowMapper<Object>) rs -> {
            try {
                return reader.getValue(rs);
            } catch (Exception e) {
                throw new SQLException(e);
            }
        }; // TODO separate inner class
    }
}
