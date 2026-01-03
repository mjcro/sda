package io.github.mjcro.sda.prefab;

import io.github.mjcro.sda.DatabaseException;
import io.github.mjcro.sda.SourceWither;
import io.github.mjcro.sda.SqlInvoker;
import io.github.mjcro.sda.SqlModifier;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.time.Instant;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Abstract class for all SQL-based repositories.
 */
public abstract class AbstractSqlRepository {
    private final Supplier<Instant> timeProvider;
    private final SqlInvoker sqlInvoker;
    private final SqlModifier sqlModifier;

    /**
     * Constructs new abstract SQL repository with given {@link SqlInvoker} or {@link SqlModifier}
     * and default current time provider.
     *
     * @param db Database connection to use, either {@link SqlInvoker} or {@link SqlModifier}.
     */
    protected AbstractSqlRepository(@NonNull SqlInvoker db) {
        this(db, null);
    }

    /**
     * Constructs new abstract SQL repository with given {@link SqlInvoker} or {@link SqlModifier}
     *
     * @param db           Database connection to use, either {@link SqlInvoker} or {@link SqlModifier}.
     * @param timeProvider Current time provider, optional.
     */
    protected AbstractSqlRepository(@NonNull SqlInvoker db, @Nullable Supplier<Instant> timeProvider) {
        this.sqlInvoker = Objects.requireNonNull(db, "db");
        this.sqlModifier = (db instanceof SqlModifier) ? (SqlModifier) db : null;
        this.timeProvider = timeProvider == null ? Instant::now : timeProvider;
    }

    /**
     * @return Current time.
     */
    protected @NonNull Instant getCurrentTime() {
        return timeProvider.get();
    }

    /**
     * @return Current time represented as epoch second (Unix timestamp).
     */
    protected long getCurrentEpochSecond() {
        return getCurrentTime().getEpochSecond();
    }

    /**
     * @return SQL invoker.
     */
    protected @NonNull SqlInvoker getSqlInvoker() {
        return sqlInvoker instanceof SourceWither<?>
                ? (SqlInvoker) ((SourceWither<?>) sqlInvoker).withSource(this)
                : sqlInvoker;
    }

    /**
     * @return SQL modifier.
     */
    protected @NonNull SqlModifier getSqlModifier() {
        if (sqlModifier == null) {
            throw new DatabaseException("no SQL modifier configured for " + getClass().getName());
        }
        return sqlModifier instanceof SourceWither<?>
                ? (SqlModifier) ((SourceWither<?>) sqlModifier).withSource(this)
                : sqlModifier;
    }
}
