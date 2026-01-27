package io.github.mjcro.sda.reflection;

import io.github.mjcro.sda.TypeHandler;
import io.github.mjcro.sda.UnsupportedAnnotatedElementException;
import org.jspecify.annotations.NonNull;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.Objects;

public class EntityConstructorTypeHandler implements TypeHandler {
    private final TypeHandler parameterTypeHandler;

    public EntityConstructorTypeHandler(@NonNull TypeHandler parameterTypeHandler) {
        this.parameterTypeHandler = Objects.requireNonNull(parameterTypeHandler, "parameterTypeHandler");
    }

    @Override
    public boolean supports(@NonNull AnnotatedElement element) {
        if (!(element instanceof Class<?>)) {
            return false;
        }

        Class<?> clazz = (Class<?>) element;
        Constructor<?>[] ctors = clazz.getDeclaredConstructors();
        if (ctors.length != 1) {
            return false; // Many constructors
        }
        if (ctors[0].getParameterCount() == 0) {
            return false; // No arg constructor
        }

        for (Parameter parameter : ctors[0].getParameters()) {
            if (!(parameterTypeHandler.supports(parameter))) {
                return false;
            }
        }

        return true;
    }

    @Override
    public ValueReader getValueReader(@NonNull AnnotatedElement element) {
        if (!supports(element)) {
            throw new UnsupportedAnnotatedElementException(this.getClass(), element);
        }
        Class<?> clazz = (Class<?>) element;
        Constructor<?> ctor = clazz.getDeclaredConstructors()[0];
        ctor.setAccessible(true);

        Parameter[] parameters = ctor.getParameters();
        TypeHandler.ValueReader[] readers = new ValueReader[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            readers[i] = parameterTypeHandler.getValueReader(parameters[i]);
        }

        return rs -> {
            Object[] args = new Object[readers.length];
            for (int i = 0; i < args.length; i++) {
                args[i] = readers[i].getValue(rs);
            }
            return ctor.newInstance(args);
        };
    }
}
