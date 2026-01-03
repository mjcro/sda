package io.github.mjcro.sda.reflection;

import io.github.mjcro.sda.FieldWriter;
import org.jspecify.annotations.NonNull;

import java.lang.reflect.Field;
import java.util.Locale;
import java.util.Optional;

public class FieldWriterProducerUppercaseEnums implements FieldWriterProducer {
    @Override
    public @NonNull Optional<FieldWriter<?>> apply(@NonNull Field field, @NonNull String columnName) {
        Class<?> fieldType = field.getType();
        if (Enum.class.isAssignableFrom(fieldType)) {
            return Optional.of((FieldWriterReflective<Object>) (rs, to) -> {
                String value = rs.getString(columnName);
                if (value == null || rs.wasNull()) {
                    field.set(to, null);
                } else {
                    //noinspection unchecked,rawtypes
                    field.set(to, Enum.valueOf((Class<? extends Enum>) fieldType, value.toUpperCase(Locale.ROOT)));
                }
            });
        }
        return Optional.empty();
    }
}
