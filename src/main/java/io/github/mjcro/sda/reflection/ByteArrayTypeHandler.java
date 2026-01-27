package io.github.mjcro.sda.reflection;

import org.jspecify.annotations.NonNull;

import java.util.Objects;

public class ByteArrayTypeHandler extends AbstractSimpleColumnTypeHandler {
    @Override
    protected boolean supports(@NonNull Class<?> clazz) {
        return Objects.equals(byte[].class, clazz);
    }

    @Override
    protected ValueReader getValueReader(@NonNull String column) {
        return rs -> rs.getBytes(column);
    }
}
