package io.github.mjcro.sda.reflection;

import org.jspecify.annotations.NonNull;

import java.util.Objects;

public class StringTypeHandler extends AbstractSimpleColumnTypeHandler {
    @Override
    protected boolean supports(@NonNull Class<?> clazz) {
        return Objects.equals(String.class, clazz);
    }

    @Override
    protected ValueReader getValueReader(@NonNull String column) {
        return rs -> rs.getString(column);
    }
}
