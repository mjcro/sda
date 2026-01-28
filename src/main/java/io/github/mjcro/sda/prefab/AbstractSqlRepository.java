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
     * Main constructor.
     *
     * @param sqlInvoker   SQL invoker to use for read-only operations.
     * @param sqlModifier  SQL invoker to user for read-writer operations, optional.
     * @param timeProvider Current time provider.
     */
    public AbstractSqlRepository(
            @NonNull SqlInvoker sqlInvoker,
            @Nullable SqlModifier sqlModifier,
            @Nullable Supplier<Instant> timeProvider
    ) {
        this.sqlInvoker = Objects.requireNonNull(sqlInvoker, "sqlInvoker");
        this.sqlModifier = sqlModifier;
        this.timeProvider = timeProvider == null ? Instant::now : timeProvider;
    }

    /**
     * Secondary simple constructor.
     * <p>
     * Constructs new abstract SQL repository with given {@link SqlInvoker} or {@link SqlModifier}
     * and default current time provider.
     *
     * @param db Database connection to use, either {@link SqlInvoker} or {@link SqlModifier}.
     */
    public AbstractSqlRepository(@NonNull SqlInvoker db) {
        this(
                db,
                db instanceof SqlModifier ? (SqlModifier) db : null,
                null
        );
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
