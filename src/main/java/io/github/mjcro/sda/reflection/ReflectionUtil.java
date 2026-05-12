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

/**
 * Static reflection helpers used by reflection-based type handlers.
 */
public class ReflectionUtil {
    /**
     * Resolves the database column name for the given annotated element by checking
     * {@link VirtualColumn} or {@link Column}.
     *
     * @param element Annotated element. Not null.
     * @return Column name, or null if none can be determined.
     */
    public static @Nullable String getColumnName(@NonNull AnnotatedElement element) {
        if (element instanceof VirtualColumn<?>) {
            return ((VirtualColumn<?>) element).getName();
        }

        Column column = element.getAnnotation(Column.class);
        return column == null
                ? null
                : column.value();
    }

    /**
     * Returns the Java type associated with an annotated element
     * ({@link Class}, {@link Field}, {@link Parameter} or {@link VirtualColumn}).
     *
     * @param element Annotated element. Not null.
     * @return Java type, or null if element kind is not recognized.
     */
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

    /**
     * Returns all declared fields of the class (including private ones, excluding inherited).
     *
     * @param clazz Class to inspect. Not null.
     * @return Mutable list of declared fields, not null.
     */
    public static ArrayList<Field> getAllFields(@NonNull Class<?> clazz) {
        ArrayList<Field> fields = new ArrayList<>();
        Collections.addAll(fields, clazz.getDeclaredFields());
        return fields;
    }

    private ReflectionUtil() {
    }
}
