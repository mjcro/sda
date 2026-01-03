package io.github.mjcro.sda.reflection;

import io.github.mjcro.sda.FieldWriter;
import org.jspecify.annotations.NonNull;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.function.BiFunction;

public interface FieldWriterProducer extends BiFunction<Field, String, Optional<FieldWriter<?>>> {
    @Override
    @NonNull Optional<FieldWriter<?>> apply(@NonNull Field field, @NonNull String s);
}
