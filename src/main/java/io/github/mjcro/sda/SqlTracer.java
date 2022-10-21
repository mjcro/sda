package io.github.mjcro.sda;

import java.time.Duration;

public interface SqlTracer {
    /**
     * Method invoked on successful or erroneous query invocation.
     *
     * @param sql          SQL query.
     * @param placeholders Placeholders.
     * @param elapsed      Elapsed time.
     * @param error        Error, optional. Will be empty on success.
     */
    void trace(
            String sql,
            Object[] placeholders,
            Duration elapsed,
            Exception error
    );
}
