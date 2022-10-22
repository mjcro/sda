package io.github.mjcro.sda.prefab;

import io.github.mjcro.sda.DatabaseException;
import io.github.mjcro.sda.SqlInvoker;
import io.github.mjcro.sda.SqlModifier;

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
    protected AbstractSqlRepository(SqlInvoker db) {
        this(db, null);
    }

    /**
     * Constructs new abstract SQL repository with given {@link SqlInvoker} or {@link SqlModifier}
     *
     * @param db           Database connection to use, either {@link SqlInvoker} or {@link SqlModifier}.
     * @param timeProvider Current time provider, optional.
     */
    protected AbstractSqlRepository(SqlInvoker db, Supplier<Instant> timeProvider) {
        this.sqlInvoker = Objects.requireNonNull(db, "db");
        this.sqlModifier = (db instanceof SqlModifier) ? (SqlModifier) db : null;
        this.timeProvider = timeProvider == null ? Instant::now : timeProvider;
    }

    /**
     * @return Current time.
     */
    protected Instant getCurrentTime() {
        return timeProvider.get();
    }

    /**
     * @return Current time represented as epoch second (unix timestamp).
     */
    protected long getCurrentEpochSecond() {
        return getCurrentTime().getEpochSecond();
    }

    /**
     * @return SQL invoker.
     */
    protected SqlInvoker getSqlInvoker() {
        return sqlInvoker;
    }

    /**
     * @return SQL modifier.
     */
    protected SqlModifier getSqlModifier() {
        if (sqlModifier == null) {
            throw new DatabaseException("no SQL modifier configured for " + getClass().getName());
        }
        return sqlModifier;
    }
}
