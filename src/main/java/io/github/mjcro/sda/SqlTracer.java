package io.github.mjcro.sda;

import java.time.Duration;

public interface SqlTracer {
    /**
     * Method invoked on successful or erroneous query invocation.
     *
     * @param source     Trace source, repository object in most cases.
     * @param sql        SQL query.
     * @param parameters Parameters.
     * @param elapsed    Elapsed time.
     * @param error      Error, optional. Will be empty on success.
     */
    void trace(
            Object source,
            String sql,
            Object[] parameters,
            Duration elapsed,
            Exception error
    );
}
