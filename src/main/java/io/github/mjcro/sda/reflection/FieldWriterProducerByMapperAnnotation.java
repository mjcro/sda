package io.github.mjcro.sda.reflection;

import io.github.mjcro.sda.FieldWriter;
import io.github.mjcro.sda.Mapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Optional;

public class FieldWriterProducerByMapperAnnotation implements FieldWriterProducer {
    @Override
    public Optional<FieldWriter<?>> apply(final Field field, final String columnName) {
        if (field.isAnnotationPresent(Mapper.class)) {
            try {
                Class<? extends FieldWriter<?>> clazz = field.getAnnotation(Mapper.class).value();
                Constructor<? extends FieldWriter<?>> constructor = clazz.getConstructor(String.class);
                constructor.setAccessible(true);
                return Optional.of(constructor.newInstance(columnName));
            } catch (SecurityException | ReflectiveOperationException e) {
                throw new DatabaseReflectiveOperationException(e);
            }
        }
        return Optional.empty();
    }
}
