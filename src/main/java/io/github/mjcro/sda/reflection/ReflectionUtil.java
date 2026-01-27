package io.github.mjcro.sda.reflection;

import io.github.mjcro.sda.Column;
import io.github.mjcro.sda.VirtualColumn;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collections;

public class ReflectionUtil {
    public static @Nullable String getColumnName(@NonNull AnnotatedElement element) {
        if (element instanceof VirtualColumn<?>) {
            return ((VirtualColumn<?>) element).getName();
        }

        Column column = element.getAnnotation(Column.class);
        return column == null
                ? null
                : column.value();
    }

    public static Class<?> getElementType(@NonNull AnnotatedElement element) {
        if (element instanceof Class<?>) {
            return (Class<?>) element;
        } else if (element instanceof Field) {
            return ((Field) element).getType();
        } else if (element instanceof Parameter) {
            return ((Parameter) element).getType();
        } else if (element instanceof VirtualColumn<?>) {
            return ((VirtualColumn<?>) element).getType();
        }
        return null;
    }

    public static ArrayList<Field> getAllFields(@NonNull Class<?> clazz) {
        ArrayList<Field> fields = new ArrayList<>();
        Collections.addAll(fields, clazz.getDeclaredFields());
        return fields;
    }

    private ReflectionUtil() {
    }
}
