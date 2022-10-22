package io.github.mjcro.sda.reflection;

import io.github.mjcro.sda.FieldWriter;

import java.lang.reflect.Field;
import java.time.Duration;
import java.util.Optional;

/**
 * Field writer that populates {@link Duration} fields using milliseconds
 * values obtained from database.
 */
public class FieldWriterProducerDurationMillis implements FieldWriterProducer {
    @Override
    public Optional<FieldWriter<?>> apply(Field field, String columnName) {
        if (field.getType() != Duration.class) {
            return Optional.empty();
        }
        return Optional.of((FieldWriterReflective<Object>) (from, to) -> field.set(to, Duration.ofMillis(from.getLong(columnName))));
    }
}
