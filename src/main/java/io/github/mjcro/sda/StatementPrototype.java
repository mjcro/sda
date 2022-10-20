package io.github.mjcro.sda;

@FunctionalInterface
public interface StatementPrototype {
    /**
     * Constructs statement from current prototype using given dialect.
     *
     * @param dialect Dialect to use.
     * @return Statement.
     */
    Statement createStatement(Dialect dialect);
}
