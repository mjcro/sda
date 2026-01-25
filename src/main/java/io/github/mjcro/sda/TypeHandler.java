package io.github.mjcro.sda;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.sql.ResultSet;

public interface TypeHandler {
    /**
     * Analyzes if given type handler can handle class for given annotated element.
     *
     * @param element Annotated element, like class, parameter or field.
     * @return True if this type handler can handle given annotated element, false othewise.
     */
    boolean supports(@NonNull AnnotatedElement element);

    /**
     * Constructs value reader for given annotated element.
     *
     * @param element Annotated element, like class, parameter or field.
     * @return Result set value reader
     * @throws UnsupportedAnnotatedElementException On attempt to get a reader for unsupported element.
     */
    ValueReader getValueReader(@NonNull AnnotatedElement element) throws UnsupportedAnnotatedElementException;

    /**
     * Constructs field setter for given field.
     *
     * @param field Field to create setter for.
     * @return Field value setter.
     * @throws UnsupportedAnnotatedElementException On attempt to get a setter for unsupported element.
     */
    FieldSetter getFieldSetter(@NonNull Field field) throws UnsupportedAnnotatedElementException;

    /**
     * Defines result set value readers - components responsible for mapping
     * result set data into domain objects/enitities.
     */
    @FunctionalInterface
    interface ValueReader {
        /**
         * Constructs value from result set.
         *
         * @param rs Source result set.
         * @return Obtained value, nullable.
         * @throws Exception On any error.
         */
        @Nullable Object getValue(@NonNull ResultSet rs) throws Exception;
    }

    /**
     * Defines field value setters - components responsible for reading
     * data from result set into object fields.
     */
    @FunctionalInterface
    interface FieldSetter {
        /**
         * Sets value from result set to target object's field.
         *
         * @param rs     Source result set.
         * @param target Target object, which field should be set with value from result set.
         * @throws Exception On any error.
         */
        void setObjectField(@NonNull ResultSet rs, @NonNull Object target) throws Exception;
    }
}
