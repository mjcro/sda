package io.github.mjcro.sda;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Forces a specific {@link TypeHandler} implementation to be used
 * when reading the annotated field or constructor parameter from a {@link java.sql.ResultSet}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
public @interface Mapper {
    /**
     * Type handler class to apply. Not null.
     *
     * @return Type handler class.
     */
    Class<? extends TypeHandler> value();
}
