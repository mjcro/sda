package io.github.mjcro.sda;

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

    protected AbstractSqlRepository(SqlInvoker db) {
        this(db, null);
    }

    protected AbstractSqlRepository(SqlInvoker db, Supplier<Instant> timeProvider) {
        this.sqlInvoker = Objects.requireNonNull(db, "db");
        this.sqlModifier = (db instanceof SqlModifier) ? (SqlModifier) db : null;
        this.timeProvider = timeProvider == null ? Instant::now : timeProvider;
    }

    protected Instant getCurrentTime() {
        return timeProvider.get();
    }

    protected long getCurrentEpochSecond() {
        return getCurrentTime().getEpochSecond();
    }

    protected SqlInvoker getSqlInvoker() {
        return sqlInvoker;
    }

    protected SqlModifier getSqlModifier() {
        if (sqlModifier == null) {
            throw new DatabaseException("no SQL modifier configured for " + getClass().getName());
        }
        return sqlModifier;
    }
}
