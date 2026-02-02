package io.github.mjcro.sda.prefab;

import io.github.mjcro.sda.SqlInvoker;
import io.github.mjcro.sda.SqlModifier;
import io.github.mjcro.sda.Statements;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.function.Supplier;

/*
Table structure:

(
    `id`   bigint unsigned NOT NULL AUTO_INCREMENT,
    `key`  varchar(255) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
    `time` bigint unsigned NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `key_UNIQUE` (`key`)
);
 */

/**
 * Experimental implementation of distributed locks using database table.
 */
public class SharedLockRepository extends AbstractSqlRepository {
    private final String table;

    /**
     * Constructor.
     *
     * @param table        Database table name.
     * @param sqlInvoker   SQL invoker.
     * @param sqlModifier  SQL modifier.
     * @param timeProvider Current time provider, optional.
     */
    public SharedLockRepository(
            @NonNull String table,
            @NonNull SqlInvoker sqlInvoker,
            @Nullable SqlModifier sqlModifier,
            @Nullable Supplier<Instant> timeProvider
    ) {
        super(sqlInvoker, sqlModifier, timeProvider);
        this.table = Objects.requireNonNull(table, "table");
    }

    /**
     * Obtains lock for given key.
     *
     * @param key Lock key.
     * @return Java {@link Lock} instance.
     */
    public Lock getLock(@NonNull String key) {
        return new DatabaseLock(key);
    }

    private /* non-static */ class DatabaseLock implements Lock {
        private final String key;
        private boolean locked = false;

        private DatabaseLock(@NonNull String key) {
            this.key = Objects.requireNonNull(key, "key");
        }

        @Override
        public void lock() {
            getSqlModifier()
                    .modify(Statements.insert(
                            table,
                            Map.of(
                                    "key", key,
                                    "locked_at", getCurrentTime().getEpochSecond()
                            )
                    ));
            locked = true;
        }

        @Override
        public void unlock() {
            if (locked) {
                locked = false;
                getSqlModifier().modify(Statements.deleteByColumn(table, "key", key));
            }
        }

        @Override
        public void lockInterruptibly() {
            throw new AssertionError("not supported");
        }

        @Override
        public boolean tryLock() {
            throw new AssertionError("not supported");
        }

        @Override
        public boolean tryLock(long time, TimeUnit unit) {
            throw new AssertionError("not supported");
        }

        @Override
        public @NonNull Condition newCondition() {
            throw new AssertionError("not supported");
        }

        @Override
        public String toString() {
            return "DatabaseLock \"" + key + "\" " + (locked ? "locked" : "unlocked");
        }
    }
}
