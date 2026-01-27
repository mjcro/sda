package io.github.mjcro.sda.reflection;

import io.github.mjcro.sda.Mapper;
import io.github.mjcro.sda.TypeHandler;
import io.github.mjcro.sda.VirtualColumn;
import org.jspecify.annotations.NonNull;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;

public class MapperAnnotationTypeHandler implements TypeHandler {
    @Override
    public boolean supports(@NonNull AnnotatedElement element) {
        return element.isAnnotationPresent(Mapper.class);
    }

    private @NonNull TypeHandler buildTypeHandler(@NonNull Mapper mapper, @NonNull AnnotatedElement element) {
        try {
            Class<? extends TypeHandler> handlerClass = mapper.value();
            for (Constructor<?> c : handlerClass.getConstructors()) {
                if (c.getParameterCount() == 0) {
                    // Found no-args constructor
                    c.setAccessible(true);
                    return (TypeHandler) c.newInstance();
                } else if (c.getParameterCount() == 1 && c.getParameters()[0].getType() == Class.class) {
                    // Found constructor requiring Class<?>
                    c.setAccessible(true);
                    Class<?> clazz;
                    if (element instanceof VirtualColumn) {
                        clazz = ((VirtualColumn<?>) element).getType();
                    } else if (element instanceof Parameter) {
                        clazz = ((Parameter) element).getType();
                    } else if (element instanceof Field) {
                        clazz = ((Field) element).getType();
                    } else {
                        throw new ReflectiveOperationException("Unable to obtain class data from " + element);
                    }

                    return (TypeHandler) c.newInstance(clazz);
                }
            }

            throw new ReflectiveOperationException("No suitable constructor found for " + handlerClass);
        } catch (ReflectiveOperationException e) {
            throw new EnvelopedReflectiveOperationException(e);
        }
    }

    @Override
    public ValueReader getValueReader(@NonNull AnnotatedElement element) {
        if (!supports(element)) {
            // TODO throw
        }
        return buildTypeHandler(element.getAnnotation(Mapper.class), element)
                .getValueReader(element);
    }
}
