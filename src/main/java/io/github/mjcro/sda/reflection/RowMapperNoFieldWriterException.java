package io.github.mjcro.sda.reflection;

import java.lang.reflect.Field;

public class RowMapperNoFieldWriterException extends DatabaseReflectiveOperationException {
    public RowMapperNoFieldWriterException(Field field) {
        super(String.format("No field writer for field %s", field));
    }
}
