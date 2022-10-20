package io.github.mjcro.sda.reflection;

import io.github.mjcro.sda.FieldWriter;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.function.BiFunction;

public interface FieldWriterProducer extends BiFunction<Field, String, Optional<FieldWriter<?>>> {
}
