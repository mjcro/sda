package io.github.mjcro.sda;

import java.time.Duration;

public interface SqlTracer {
    /**
     * Method invoked on successful or erroneous query invocation.
     *
     * @param sql        SQL query.
     * @param parameters Parameters.
     * @param elapsed    Elapsed time.
     * @param error      Error, optional. Will be empty on success.
     */
    void trace(
            String sql,
            Object[] parameters,
            Duration elapsed,
            Exception error
    );
}
