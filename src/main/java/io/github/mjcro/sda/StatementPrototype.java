package io.github.mjcro.sda;

import io.github.mjcro.interfaces.database.Statement;
import org.jspecify.annotations.NonNull;

@FunctionalInterface
public interface StatementPrototype {
    /**
     * Constructs statement from current prototype using given dialect.
     *
     * @param dialect Dialect to use.
     * @return Statement.
     */
    @NonNull Statement createStatement(@NonNull Dialect dialect);
}
