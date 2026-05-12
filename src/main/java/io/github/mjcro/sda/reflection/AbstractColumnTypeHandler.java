package io.github.mjcro.sda.reflection;

import io.github.mjcro.sda.TypeHandler;
import io.github.mjcro.sda.UnsupportedAnnotatedElementException;
import org.jspecify.annotations.NonNull;

import java.lang.reflect.AnnotatedElement;

/**
 * Base class for {@link TypeHandler}s that read a single column whose name is taken
 * from {@link io.github.mjcro.sda.Column} (or a {@link io.github.mjcro.sda.VirtualColumn}).
 * Subclasses decide which Java type they handle and how to read it.
 */
public abstract class AbstractColumnTypeHandler implements TypeHandler {
    @Override
    public final boolean supports(@NonNull AnnotatedElement element) {
        String columnName = ReflectionUtil.getColumnName(element);
        Class<?> columnType = ReflectionUtil.getElementType(element);

        return columnName != null && columnType != null && supports(columnType);
    }

    @Override
    public final ValueReader getValueReader(@NonNull AnnotatedElement element) {
        if (supports(element)) {
            String columnName = ReflectionUtil.getColumnName(element);
            Class<?> columnType = ReflectionUtil.getElementType(element);

            if (columnName != null && columnType != null) {
                return getValueReader(columnName, columnType);
            }
        }
        throw new UnsupportedAnnotatedElementException(this.getClass(), element);
    }

    /**
     * @param clazz Java type associated with the column. Not null.
     * @return True if this handler can read values of the given type.
     */
    protected abstract boolean supports(@NonNull Class<?> clazz);

    /**
     * Builds a reader for the given column and value type.
     *
     * @param column Column name. Not null.
     * @param clazz  Target Java type. Not null.
     * @return Value reader, not null.
     */
    protected abstract ValueReader getValueReader(@NonNull String column, @NonNull Class<?> clazz);
}