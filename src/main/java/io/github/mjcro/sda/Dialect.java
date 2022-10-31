package io.github.mjcro.sda;

/**
 * Defines database dialect.
 * Used in {@link StatementPrototype} to generate statements;
 */
@FunctionalInterface
public interface Dialect {
    Dialect H2 = new DialectH2();
    Dialect MySQL = new DialectMySql();

    String getName();

    /**
     * Compares compatibilities of two dialects.
     *
     * @param other Dialect to compare with;
     * @return True if both dialects are same or compatible.
     */
    default boolean isCompatibleWith(Dialect other) {
        return other == this;
    }
}
