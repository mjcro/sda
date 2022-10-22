package io.github.mjcro.sda.prefab;

import io.github.mjcro.sda.SqlInvoker;
import io.github.mjcro.sda.SqlModifier;
import io.github.mjcro.sda.Statement;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Abstract SQL repository that works with one entity and one database table.
 * Primary keys are long.
 *
 * @param <T> Entity class.
 */
public class AbstractSqlLongIdRepository<T> extends AbstractSqlSingleEntityRepository<T> {
    /**
     * Constructs new abstract SQL repository with given {@link SqlInvoker} or {@link SqlModifier}
     * and default current time provider.
     *
     * @param db          Database connection to use, either {@link SqlInvoker} or {@link SqlModifier}.
     * @param entityClass Repository entity class.
     * @param tableName   Database table name.
     */
    protected AbstractSqlLongIdRepository(
            SqlInvoker db,
            Class<? extends T> entityClass,
            String tableName
    ) {
        super(db, entityClass, tableName);
    }

    /**
     * Constructs new abstract SQL repository with given {@link SqlInvoker} or {@link SqlModifier}
     *
     * @param db           Database connection to use, either {@link SqlInvoker} or {@link SqlModifier}.
     * @param entityClass  Repository entity class.
     * @param tableName    Database table name.
     * @param timeProvider Current time provider, optional.
     */
    protected AbstractSqlLongIdRepository(
            SqlInvoker db,
            Class<? extends T> entityClass,
            String tableName,
            Supplier<Instant> timeProvider
    ) {
        super(db, entityClass, tableName, timeProvider);
    }

    public List<T> findById(final long... ids) {
        if (ids == null || ids.length == 0) {
            return List.of();
        }

        return getSqlInvoker().list(
                getEntityClass(),
                Statement.matchColumn(getTableName(), "id", Arrays.stream(ids).boxed().collect(Collectors.toSet()))
        );
    }
}
