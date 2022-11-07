package io.github.mjcro.sda.reflection;

import io.github.mjcro.references.longs.IdReference;
import io.github.mjcro.sda.FieldWriter;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Optional;

public class FieldWriterProducerIdEnum implements FieldWriterProducer {
    @Override
    public Optional<FieldWriter<?>> apply(final Field field, final String columnName) {
        Class<?> fieldType = field.getType();
        if (Enum.class.isAssignableFrom(fieldType) && IdReference.class.isAssignableFrom(fieldType)) {
            // Building values map
            HashMap<Long, Object> values = new HashMap<>();
            for (Object cc : fieldType.getEnumConstants()) {
                values.put(((IdReference) cc).getId(), cc);
            }
            return Optional.of((FieldWriterReflective<Object>) (rs, to) -> field.set(to, values.get(rs.getLong(columnName))));
        }
        return Optional.empty();
    }
}
