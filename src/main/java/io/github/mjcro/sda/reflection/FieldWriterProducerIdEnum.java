package io.github.mjcro.sda.reflection;

import io.github.mjcro.references.longs.IdReference;

import java.util.HashMap;
import java.util.Optional;

public class FieldWriterProducerIdEnum extends AbstractClassCachingProducer {
    @Override
    public boolean isSupported(Class<?> clazz) {
        return Enum.class.isAssignableFrom(clazz) && IdReference.class.isAssignableFrom(clazz);
    }

    @Override
    public FieldWriterProducer initializeCache(Class<?> clazz) {
        // Building values map
        HashMap<Long, Object> values = new HashMap<>();
        for (Object cc : clazz.getEnumConstants()) {
            values.put(((IdReference) cc).getId(), cc);
        }
        return (field, columnName) -> Optional.of((FieldWriterReflective<Object>) (rs, to) -> field.set(to, values.get(rs.getLong(columnName))));
    }
}
