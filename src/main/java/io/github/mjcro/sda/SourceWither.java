package io.github.mjcro.sda;

public interface SourceWither<T> {
    /**
     * Returns new instance of same class but with source parameter injected.
     *
     * @param source Source.
     * @return New instance.
     */
    T withSource(Object source);
}
