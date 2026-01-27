package io.github.mjcro.sda.reflection;

import org.jspecify.annotations.NonNull;

import java.util.HashMap;
import java.util.Locale;

public class EnumNameTypeHandler extends AbstractColumnTypeHandler {
    @Override
    protected boolean supports(@NonNull Class<?> clazz) {
        return Enum.class.isAssignableFrom(clazz);
    }

    @Override
    protected ValueReader getValueReader(@NonNull String column, @NonNull Class<?> clazz) {
        // Building values map
        HashMap<String, Object> values = new HashMap<>();

        for (Object o : clazz.getEnumConstants()) {
            values.put(
                    ((Enum<?>) o).name().toUpperCase(Locale.ROOT),
                    o
            );
        }

        return rs -> {
            String value = rs.getString(column);
            return value == null
                    ? null
                    : values.get(value.toUpperCase(Locale.ROOT));
        };
    }
}
