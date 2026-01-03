package io.github.mjcro.sda.reflection;

import org.jspecify.annotations.Nullable;

import java.lang.reflect.Field;

public class RowMapperNoFieldWriterException extends DatabaseReflectiveOperationException {
    public RowMapperNoFieldWriterException(@Nullable Field field) {
        super(String.format("No field writer for field %s", field));
    }
}
