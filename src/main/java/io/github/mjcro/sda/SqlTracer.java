package io.github.mjcro.sda;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.time.Duration;

/**
 * Observability hook that receives a callback after every SQL invocation
 * performed by an {@link SqlInvoker}. Used for logging, metrics, and tracing.
 */
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
            @NonNull Object source,
            @NonNull String sql,
            @Nullable Object @Nullable [] parameters,
            @NonNull Duration elapsed,
            @Nullable Exception error
    );
}
