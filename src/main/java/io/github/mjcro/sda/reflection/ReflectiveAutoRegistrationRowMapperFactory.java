package io.github.mjcro.sda.reflection;

import io.github.mjcro.sda.BasicRowMapperFactory;
import io.github.mjcro.sda.Column;
import io.github.mjcro.sda.FieldWriter;
import io.github.mjcro.sda.RowMapper;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Row mapper factory that produces row mapper using reflection.
 */
public class ReflectiveAutoRegistrationRowMapperFactory extends BasicRowMapperFactory {
    private final FieldWriterProducer writerProducer;

    /**
     * Constructs row mapper factory with given field writer producers.
     *
     * @param others Field writer producers to use.
     * @return Row mapper factory.
     */
    public static BasicRowMapperFactory standard(@NonNull FieldWriterProducer @Nullable ... others) {
        FieldWriterProducerSequentialList list = new FieldWriterProducerSequentialList(List.of(
                new FieldWriterProducerByMapperAnnotation(),
                new FieldWriterProducerByCreatorAnnotation(),
                new FieldWriterProducerPrimitives(),
                new FieldWriterProducerBoxedPrimitives(),
                new FieldWriterProducerUppercaseEnums()
        ));

        if (others != null && others.length > 0) {
            for (FieldWriterProducer other : others) {
                list.add(other);
            }
        }

        return new ReflectiveAutoRegistrationRowMapperFactory(list);
    }

    /**
     * Recursively (over inheritance) iterates over all declared fields.
     *
     * @param clazz Class to start recursion from.
     * @return Found fields.
     */
    static @NonNull List<Field> recursiveFields(@NonNull Class<?> clazz) {
        if (clazz == Object.class) {
            return List.of();
        }

        ArrayList<Field> fields = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers())) {
                continue;
            }
            fields.add(field);
        }
        fields.addAll(recursiveFields(clazz.getSuperclass()));
        return fields;
    }

    public ReflectiveAutoRegistrationRowMapperFactory(@NonNull FieldWriterProducer writerProducer) {
        this.writerProducer = Objects.requireNonNull(writerProducer, "writerProducer");
    }

    @Override
    public @NonNull <T> RowMapper<T> get(@NonNull Class<T> clazz) {
        if (!contains(clazz)) {
            register(clazz, create(clazz));
        }

        return super.get(clazz);
    }

    @SuppressWarnings("unchecked")
    public @NonNull <T> RowMapper<T> create(@NonNull Class<T> clazz) {
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

    public @NonNull FieldWriter<?> getFieldWriter(@NonNull Field field) {
        field.setAccessible(true);
        String columnName = field.isAnnotationPresent(Column.class)
                ? field.getAnnotation(Column.class).value()
                : field.getName();
        return writerProducer.apply(field, columnName)
                .orElseThrow(() -> new RowMapperNoFieldWriterException(field));
    }

    private static final class ConstructorObjectSupplier<T> implements Supplier<T> {
        private final Constructor<T> constructor;

        private ConstructorObjectSupplier(@NonNull Constructor<T> constructor) {
            Objects.requireNonNull(constructor, "constructor");
            this.constructor = constructor;
            this.constructor.setAccessible(true);
        }

        @Override
        public @NonNull T get() {
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

        private ManagedRowMapper(@NonNull Class<T> clazz, @NonNull Supplier<T> constructor, @NonNull FieldWriter<T>[] writers) {
            this.clazz = clazz;
            this.constructor = constructor;
            this.writers = writers;
        }

        @Override
        public @NonNull T mapRow(@NonNull ResultSet rs) throws SQLException {
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
