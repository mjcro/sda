package io.github.mjcro.sda.prefab;

import io.github.mjcro.interfaces.database.Statement;
import io.github.mjcro.sda.SqlInvoker;
import io.github.mjcro.sda.SqlModifier;
import io.github.mjcro.sda.StatementPrototype;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

public abstract class AbstractEntitySqlRepository<E> extends AbstractSqlRepository {
    private final Class<E> entityClass;

    /**
     * Main constructor.
     *
     * @param sqlInvoker   SQL invoker to use for read-only operations.
     * @param sqlModifier  SQL invoker to user for read-writer operations, optional.
     * @param timeProvider Current time provider.
     * @param entityClass  Entity class.
     */
    public AbstractEntitySqlRepository(
            @NonNull SqlInvoker sqlInvoker,
            @Nullable SqlModifier sqlModifier,
            @Nullable Supplier<Instant> timeProvider,
            @NonNull Class<E> entityClass
    ) {
        super(sqlInvoker, sqlModifier, timeProvider);
        this.entityClass = Objects.requireNonNull(entityClass, "entityClass");
    }

    /**
     * Secondary simple constructor.
     * <p>
     * Constructs new abstract SQL repository with given {@link SqlInvoker} or {@link SqlModifier}
     * and default current time provider.
     *
     * @param db          Database connection to use, either {@link SqlInvoker} or {@link SqlModifier}.
     * @param entityClass Entity class.
     */
    public AbstractEntitySqlRepository(@NonNull SqlInvoker db, @NonNull Class<E> entityClass) {
        super(db);
        this.entityClass = Objects.requireNonNull(entityClass, "entityClass");
    }

    /**
     * Fetches single entity using given statement.
     *
     * @param stmt Statement.
     * @return Matched entity, if any.
     */
    protected @NonNull Optional<E> fetchEntity(@NonNull StatementPrototype stmt) {
        return getSqlInvoker().one(entityClass, stmt);
    }

    /**
     * Fetches single entity using given statement.
     *
     * @param stmt Statement.
     * @return Matched entity, if any.
     */
    protected @NonNull Optional<E> fetchEntity(@NonNull Statement stmt) {
        return getSqlInvoker().one(entityClass, stmt);
    }

    /**
     * Fetches list of entities.
     *
     * @param stmt Statement.
     * @return Collection of entities.
     */
    protected @NonNull List<E> fetchEntityList(@NonNull StatementPrototype stmt) {
        return getSqlInvoker().list(entityClass, stmt);
    }

    /**
     * Fetches list of entities.
     *
     * @param stmt Statement.
     * @return Collection of entities.
     */
    protected @NonNull List<E> fetchEntityList(@NonNull Statement stmt) {
        return getSqlInvoker().list(entityClass, stmt);
    }
}
