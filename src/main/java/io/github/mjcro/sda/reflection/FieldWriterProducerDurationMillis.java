package io.github.mjcro.sda.reflection;

import io.github.mjcro.sda.FieldWriter;
import org.jspecify.annotations.NonNull;

import java.lang.reflect.Field;
import java.time.Duration;
import java.util.Optional;

/**
 * Field writer that populates {@link Duration} fields using milliseconds
 * values obtained from database.
 */
public class FieldWriterProducerDurationMillis implements FieldWriterProducer {
    @Override
    public @NonNull Optional<FieldWriter<?>> apply(@NonNull Field field, @NonNull String columnName) {
        if (field.getType() != Duration.class) {
            return Optional.empty();
        }
        return Optional.of((FieldWriterReflective<Object>) (from, to) -> field.set(to, Duration.ofMillis(from.getLong(columnName))));
    }
}
