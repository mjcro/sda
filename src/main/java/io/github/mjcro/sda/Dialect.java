package io.github.mjcro.sda;

/**
 * Defines database dialect.
 * Used in {@link StatementPrototype} to generate statements;
 */
@FunctionalInterface
public interface Dialect {
    Dialect H2 = () -> "H2";
    Dialect MySQL = () -> "MySQL";

    String getName();
}
