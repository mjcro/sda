package io.github.mjcro.sda;

import org.jspecify.annotations.NonNull;

public interface SourceWither<T> {
    /**
     * Returns new instance of same class but with source parameter injected.
     *
     * @param source Source.
     * @return New instance.
     */
    @NonNull T withSource(@NonNull Object source);
}
