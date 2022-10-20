package io.github.mjcro.sda.reflection;

import io.github.mjcro.sda.BasicRowMapperFactory;
import io.github.mjcro.sda.Column;
import io.github.mjcro.sda.FieldWriter;
import io.github.mjcro.sda.RowMapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public class ReflectiveAutoRegistrationRowMapperFactory extends BasicRowMapperFactory {
    private final FieldWriterProducer writerProducer;

    public ReflectiveAutoRegistrationRowMapperFactory(FieldWriterProducer writerProducer) {
        this.writerProducer = Objects.requireNonNull(writerProducer, "writerProducer");
    }

    public static BasicRowMapperFactory standard(FieldWriterProducer... others) {
        FieldWriterProducerSequentialList list = new FieldWriterProducerSequentialList(List.of(
                new FieldWriterProducerByMapperAnnotation(),
                new FieldWriterProducerPrimitives(),
                new FieldWriterProducerBoxedPrimitives()
        ));

        if (others != null && others.length > 0) {
            for (final FieldWriterProducer other : others) {
                list.add(other);
            }
        }

        return new ReflectiveAutoRegistrationRowMapperFactory(list);
    }

    private List<Field> recursiveFields(final Class<?> clazz) {
        if (clazz == Object.class) {
            return List.of();
        }

        ArrayList<Field> fields = new ArrayList<>();
        for (final Field field : clazz.getDeclaredFields()) {
            fields.add(field);
        }
        fields.addAll(recursiveFields(clazz.getSuperclass()));
        return fields;
    }

    @Override
    public <T> RowMapper<T> get(Class<T> clazz) {
        if (!contains(clazz)) {
            RowMapper<T> rowMapper = create(clazz);
            if (rowMapper != null) {
                register(clazz, rowMapper);
            }
        }

        return super.get(clazz);
    }

    @SuppressWarnings("unchecked")
    public <T> RowMapper<T> create(Class<T> clazz) {
        Objects.requireNonNull(clazz, "clazz");
        // Determining constructor
        Supplier<T> objectSupplier;
        try {
            objectSupplier = new ConstructorObjectSupplier<>(clazz.getDeclaredConstructor());
        } catch (NoSuchMethodException e) {
            throw new DatabaseReflectiveOperationException(e);
        }

        // Reading all fields of class
        ArrayList<FieldWriter<?>> writers = new ArrayList<>();
        for (Field field : recursiveFields(clazz)) {
            FieldWriter<?> writer = getFieldWriter(field);
            writers.add(writer);
        }

        return new ManagedRowMapper<T>(
                clazz,
                objectSupplier,
                writers.toArray(new FieldWriter[0])
        );
    }

    public FieldWriter<?> getFieldWriter(Field field) {
        field.setAccessible(true);
        String columnName = field.isAnnotationPresent(Column.class)
                ? field.getAnnotation(Column.class).value()
                : field.getName();
        return writerProducer.apply(field, columnName)
                .orElseThrow(() -> new RowMapperNoFieldWriterException(field));
    }

    private static final class ConstructorObjectSupplier<T> implements Supplier<T> {
        private final Constructor<T> constructor;

        private ConstructorObjectSupplier(Constructor<T> constructor) {
            Objects.requireNonNull(constructor, "constructor");
            this.constructor = constructor;
            this.constructor.setAccessible(true);
        }

        @Override
        public T get() {
            try {
                return constructor.newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new DatabaseReflectiveOperationException(e);
            }
        }
    }

    private static final class ManagedRowMapper<T> implements RowMapper<T> {
        private final Class<T> clazz;
        private final Supplier<T> constructor;
        private final FieldWriter<T>[] writers;

        private ManagedRowMapper(Class<T> clazz, Supplier<T> constructor, FieldWriter<T>[] writers) {
            this.clazz = clazz;
            this.constructor = constructor;
            this.writers = writers;
        }

        @Override
        public T mapRow(ResultSet rs) throws SQLException {
            // Constructing instance
            T t = constructor.get();

            // Applying writers
            for (FieldWriter<T> writer : writers) {
                writer.write(rs, t);
            }

            return t;
        }

        @Override
        public String toString() {
            return "Row mapper for " + clazz.getName();
        }
    }
}
