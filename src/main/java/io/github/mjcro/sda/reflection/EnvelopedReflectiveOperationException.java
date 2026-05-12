package io.github.mjcro.sda.reflection;

import org.jspecify.annotations.NonNull;

/**
 * Unchecked wrapper that lets reflection-based type handlers propagate
 * {@link ReflectiveOperationException} without polluting their signatures.
 */
public class EnvelopedReflectiveOperationException extends IllegalStateException {
    /**
     * @param e Underlying reflective failure. Not null.
     */
    public EnvelopedReflectiveOperationException(@NonNull ReflectiveOperationException e) {
        super(e);
    }
}
