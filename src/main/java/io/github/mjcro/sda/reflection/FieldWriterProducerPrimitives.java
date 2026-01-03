package io.github.mjcro.sda.reflection;

import io.github.mjcro.sda.FieldWriter;
import org.jspecify.annotations.NonNull;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

public class FieldWriterProducerPrimitives implements FieldWriterProducer {
    private static final Map<Class<?>, FieldWriterProducer> primitives = Map.of(
            String.class, (field, column) -> Optional.of((FieldWriterReflective<Object>) (from, to) -> field.set(to, from.getString(column))),
            BigDecimal.class, (field, column) -> Optional.of((FieldWriterReflective<Object>) (from, to) -> field.set(to, from.getBigDecimal(column))),
            byte[].class, (field, column) -> Optional.of((FieldWriterReflective<Object>) (from, to) -> field.set(to, from.getBytes(column))),
            float.class, (field, column) -> Optional.of((FieldWriterReflective<Object>) (from, to) -> field.setFloat(to, from.getFloat(column))),
            double.class, (field, column) -> Optional.of((FieldWriterReflective<Object>) (from, to) -> field.setDouble(to, from.getDouble(column))),
            int.class, (field, column) -> Optional.of((FieldWriterReflective<Object>) (from, to) -> field.setInt(to, from.getInt(column))),
            short.class, (field, column) -> Optional.of((FieldWriterReflective<Object>) (from, to) -> field.setShort(to, from.getShort(column))),
            long.class, (field, column) -> Optional.of((FieldWriterReflective<Object>) (from, to) -> field.setLong(to, from.getLong(column)))
    );

    @Override
    public @NonNull Optional<FieldWriter<?>> apply(@NonNull final Field field, @NonNull final String columnName) {
        FieldWriterProducer producer = primitives.get(field.getType());
        if (producer != null) {
            return producer.apply(field, columnName);
        }

        return Optional.empty();
    }
}
