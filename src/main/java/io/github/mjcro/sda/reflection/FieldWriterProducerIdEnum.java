package io.github.mjcro.sda.reflection;

import java.util.HashMap;
import java.util.Optional;

public class FieldWriterProducerIdEnum extends AbstractClassCachingProducer {
    @Override
    public boolean isSupported(Class<?> clazz) {
        return Enum.class.isAssignableFrom(clazz) && (
                io.github.mjcro.interfaces.ints.WithId.class.isAssignableFrom(clazz)
                        || io.github.mjcro.interfaces.longs.WithId.class.isAssignableFrom(clazz)
        );
    }

    @Override
    public FieldWriterProducer initializeCache(Class<?> clazz) {
        // Building values map
        HashMap<Long, Object> values = new HashMap<>();
        for (Object cc : clazz.getEnumConstants()) {
            long value;
            if (cc instanceof io.github.mjcro.interfaces.ints.WithId) {
                value = ((io.github.mjcro.interfaces.ints.WithId) cc).getId();
            } else if (cc instanceof io.github.mjcro.interfaces.longs.WithId) {
                value = ((io.github.mjcro.interfaces.longs.WithId) cc).getId();
            } else {
                throw new RuntimeException("Unsupported type " + cc.getClass());
            }

            values.put(value, cc);
        }
        return (field, columnName) -> Optional.of((FieldWriterReflective<Object>) (rs, to) -> field.set(to, values.get(rs.getLong(columnName))));
    }
}
