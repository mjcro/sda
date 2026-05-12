package io.github.mjcro.sda.reflection;

import org.jspecify.annotations.NonNull;

/**
 * Convenience base class for column type handlers whose reader depends only on the column name,
 * not on the target Java type.
 */
public abstract class AbstractSimpleColumnTypeHandler extends AbstractColumnTypeHandler {
    @Override
    protected final ValueReader getValueReader(@NonNull String column, @NonNull Class<?> clazz) {
        return getValueReader(column);
    }

    protected abstract boolean supports(@NonNull Class<?> clazz);

    /**
     * Builds a reader for the given column.
     *
     * @param column Column name. Not null.
     * @return Value reader, not null.
     */
    protected abstract ValueReader getValueReader(@NonNull String column);
}
