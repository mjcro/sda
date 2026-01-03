package io.github.mjcro.sda.prefab;

import io.github.mjcro.sda.SqlInvoker;
import io.github.mjcro.sda.SqlModifier;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.time.Instant;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Abstract SQL repository that works with one entity and one database table.
 *
 * @param <T> Entity class.
 */
public abstract class AbstractSqlSingleEntityRepository<T> extends AbstractSqlRepository {
    private final Class<? extends T> entityClass;
    private final String tableName;

    /**
     * Constructs new abstract SQL repository with given {@link SqlInvoker} or {@link SqlModifier}
     * and default current time provider.
     *
     * @param db          Database connection to use, either {@link SqlInvoker} or {@link SqlModifier}.
     * @param entityClass Repository entity class.
     * @param tableName   Database table name.
     */
    protected AbstractSqlSingleEntityRepository(
            @NonNull SqlInvoker db,
            @NonNull Class<? extends T> entityClass,
            @NonNull String tableName
    ) {
        this(db, entityClass, tableName, null);
    }

    /**
     * Constructs new abstract SQL repository with given {@link SqlInvoker} or {@link SqlModifier}
     *
     * @param db           Database connection to use, either {@link SqlInvoker} or {@link SqlModifier}.
     * @param entityClass  Repository entity class.
     * @param tableName    Database table name.
     * @param timeProvider Current time provider, optional.
     */
    protected AbstractSqlSingleEntityRepository(
            @NonNull SqlInvoker db,
            @NonNull Class<? extends T> entityClass,
            @NonNull String tableName,
            @Nullable Supplier<Instant> timeProvider
    ) {
        super(db, timeProvider);
        this.entityClass = Objects.requireNonNull(entityClass, "entityClass");
        this.tableName = Objects.requireNonNull(tableName, "tableName");
    }

    /**
     * @return Entity class this repository works with.
     */
    public @NonNull Class<? extends T> getEntityClass() {
        return this.entityClass;
    }

    /**
     * @return Database table name.
     */
    public @NonNull String getTableName() {
        return this.tableName;
    }
}
