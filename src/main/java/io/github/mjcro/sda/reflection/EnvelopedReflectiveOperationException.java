package io.github.mjcro.sda.reflection;

import org.jspecify.annotations.NonNull;

public class EnvelopedReflectiveOperationException extends IllegalStateException {
    public EnvelopedReflectiveOperationException(@NonNull ReflectiveOperationException e) {
        super(e);
    }
}
