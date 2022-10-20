package io.github.mjcro.sda.reflection;

import io.github.mjcro.sda.FieldWriter;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.Optional;

public class FieldWriterProducerInstantSeconds implements FieldWriterProducer {
    @Override
    public Optional<FieldWriter<?>> apply(Field field, String columnName) {
        if (field.getType() != Instant.class) {
            return Optional.empty();
        }
        return Optional.of((FieldWriterReflective<Object>) (from, to) -> field.set(to, Instant.ofEpochSecond(from.getLong(columnName))));
    }
}
