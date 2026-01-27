package io.github.mjcro.sda;

import io.github.mjcro.interfaces.strings.WithName;
import org.jspecify.annotations.NonNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Objects;

public class VirtualColumn<T> implements WithName, AnnotatedElement {
    private final String name;
    private final Class<T> type;

    public VirtualColumn(@NonNull String name, @NonNull Class<T> type) {
        this.name = Objects.requireNonNull(name, "name");
        this.type = Objects.requireNonNull(type, "type");
    }

    public @NonNull Class<T> getType() {
        return type;
    }

    public @NonNull String getName() {
        return name;
    }

    @Override
    public <A extends Annotation> A getAnnotation(@NonNull Class<A> annotationClass) {
        return null;
    }

    @Override
    public @NonNull Annotation[] getAnnotations() {
        return new Annotation[0];
    }

    @Override
    public @NonNull Annotation[] getDeclaredAnnotations() {
        return new Annotation[0];
    }
}
