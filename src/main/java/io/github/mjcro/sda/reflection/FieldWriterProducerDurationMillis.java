package io.github.mjcro.sda.reflection;

import io.github.mjcro.sda.FieldWriter;

import java.lang.reflect.Field;
import java.time.Duration;
import java.util.Optional;

public class FieldWriterProducerDurationMillis implements FieldWriterProducer {
    @Override
    public Optional<FieldWriter<?>> apply(final Field field, final String columnName) {
        if (field.getType() != Duration.class) {
            return Optional.empty();
        }
        return Optional.of((FieldWriterReflective<Object>) (from, to) -> field.set(to, Duration.ofMillis(from.getLong(columnName))));
    }
}
