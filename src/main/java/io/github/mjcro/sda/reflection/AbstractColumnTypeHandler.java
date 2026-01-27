package io.github.mjcro.sda.reflection;

import io.github.mjcro.sda.TypeHandler;
import io.github.mjcro.sda.UnsupportedAnnotatedElementException;
import org.jspecify.annotations.NonNull;

import java.lang.reflect.AnnotatedElement;

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

    protected abstract boolean supports(@NonNull Class<?> clazz);

    protected abstract ValueReader getValueReader(@NonNull String column, @NonNull Class<?> clazz);
}