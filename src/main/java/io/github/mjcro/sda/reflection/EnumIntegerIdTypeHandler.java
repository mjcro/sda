package io.github.mjcro.sda.reflection;

import io.github.mjcro.interfaces.ints.WithId;
import org.jspecify.annotations.NonNull;

import java.util.HashMap;

public class EnumIntegerIdTypeHandler extends AbstractColumnTypeHandler {
    @Override
    protected boolean supports(@NonNull Class<?> clazz) {
        return Enum.class.isAssignableFrom(clazz) && WithId.class.isAssignableFrom(clazz);
    }

    @Override
    protected ValueReader getValueReader(@NonNull String column, @NonNull Class<?> clazz) {
        // Building values map
        HashMap<Integer, Object> values = new HashMap<>();

        for (Object o : clazz.getEnumConstants()) {
            values.put(
                    ((WithId) o).getId(),
                    o
            );
        }

        return rs -> {
            int value = rs.getInt(column);
            return rs.wasNull()
                    ? null
                    : values.get(value);
        };
    }
}
