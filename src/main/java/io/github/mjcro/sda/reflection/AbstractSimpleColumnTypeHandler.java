package io.github.mjcro.sda.reflection;

import org.jspecify.annotations.NonNull;

public abstract class AbstractSimpleColumnTypeHandler extends AbstractColumnTypeHandler {
    @Override
    protected final ValueReader getValueReader(@NonNull String column, @NonNull Class<?> clazz) {
        return getValueReader(column);
    }

    protected abstract boolean supports(@NonNull Class<?> clazz);

    protected abstract ValueReader getValueReader(@NonNull String column);
}
