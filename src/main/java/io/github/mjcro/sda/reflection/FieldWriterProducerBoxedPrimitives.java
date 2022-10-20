package io.github.mjcro.sda.reflection;

import io.github.mjcro.sda.FieldWriter;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;

public class FieldWriterProducerBoxedPrimitives implements FieldWriterProducer {
    private static final Map<Class<?>, FieldWriterProducer> primitives = Map.of(
            Float.class, (field, column) -> Optional.of((FieldWriterReflective<Object>) (from, to) -> {
                float value = from.getFloat(column);
                if (from.wasNull()) field.set(to, null);
                else field.set(to, value);
            }),
            Double.class, (field, column) -> Optional.of((FieldWriterReflective<Object>) (from, to) -> {
                double value = from.getDouble(column);
                if (from.wasNull()) field.set(to, null);
                else field.set(to, value);
            }),
            Integer.class, (field, column) -> Optional.of((FieldWriterReflective<Object>) (from, to) -> {
                int value = from.getInt(column);
                if (from.wasNull()) field.set(to, null);
                else field.set(to, value);
            }),
            Short.class, (field, column) -> Optional.of((FieldWriterReflective<Object>) (from, to) -> {
                short value = from.getShort(column);
                if (from.wasNull()) field.set(to, null);
                else field.set(to, value);
            }),
            Long.class, (field, column) -> Optional.of((FieldWriterReflective<Object>) (from, to) -> {
                long value = from.getLong(column);
                if (from.wasNull()) field.set(to, null);
                else field.set(to, value);
            })
    );

    @Override
    public Optional<FieldWriter<?>> apply(final Field field, final String columnName) {
        FieldWriterProducer producer = primitives.get(field.getType());
        if (producer != null) {
            return producer.apply(field, columnName);
        }

        return Optional.empty();
    }
}
