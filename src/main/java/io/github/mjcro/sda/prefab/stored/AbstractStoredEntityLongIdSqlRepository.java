package io.github.mjcro.sda.prefab.stored;

import io.github.mjcro.interfaces.database.Statement;
import io.github.mjcro.interfaces.longs.WithId;
import io.github.mjcro.sda.SqlInvoker;
import io.github.mjcro.sda.SqlModifier;
import io.github.mjcro.sda.StatementPrototype;
import io.github.mjcro.sda.Statements;
import io.github.mjcro.sda.prefab.AbstractEntitySqlRepository;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class AbstractStoredEntityLongIdSqlRepository<I extends WithId, T, E extends Stored.Convertible<I, T>> extends AbstractEntitySqlRepository<E> {
    private final String table;

    /**
     * Main constructor.
     *
     * @param sqlInvoker   SQL invoker to use for read-only operations.
     * @param sqlModifier  SQL invoker to user for read-writer operations, optional.
     * @param timeProvider Current time provider.
     * @param entityClass  Entity class.
     * @param table        Database table name.
     */
    public AbstractStoredEntityLongIdSqlRepository(
            @NonNull SqlInvoker sqlInvoker,
            @Nullable SqlModifier sqlModifier,
            @Nullable Supplier<Instant> timeProvider,
            @NonNull Class<E> entityClass,
            @NonNull String table
    ) {
        super(sqlInvoker, sqlModifier, timeProvider, entityClass);
        this.table = Objects.requireNonNull(table, "table");
    }

    /**
     * Secondary simple constructor.
     * <p>
     * Constructs new abstract SQL repository with given {@link SqlInvoker} or {@link SqlModifier}
     * and default current time provider.
     *
     * @param db          Database connection to use, either {@link SqlInvoker} or {@link SqlModifier}.
     * @param entityClass Entity class.
     * @param table       Database table name.
     */
    public AbstractStoredEntityLongIdSqlRepository(
            @NonNull SqlInvoker db,
            @NonNull Class<E> entityClass,
            @NonNull String table
    ) {
        super(db, entityClass);
        this.table = Objects.requireNonNull(table, "table");
    }

    /**
     * @return Database table name.
     */
    protected String getTable() {
        return table;
    }

    /**
     * Extracts long value from identifiers and represents result as java.lang.Object collection.
     *
     * @param ids Identifiers.
     * @return Object collection.
     */
    protected Collection<Object> objectify(@NonNull Collection<? extends WithId> ids) {
        return ids.stream().map(WithId::getId).map($ -> (Object) $).collect(Collectors.toSet());
    }

    /**
     * Finds all records by identifiers.
     *
     * @param ids Identifiers to match.
     * @return Matched records.
     */
    public List<Stored<I, T>> findById(@Nullable Collection<I> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        return fetchList(Statements.matchColumn(getTable(), "id", objectify(ids)));
    }

    /**
     * Finds single record by identifier.
     *
     * @param id Identifier to match.
     * @return Record, if any.
     */
    public final Optional<Stored<I, T>> findById(@Nullable I id) {
        return id == null
                ? Optional.empty()
                : findById(Collections.singleton(id)).stream().findAny();
    }

    /**
     * Inserts record into database and returns it's identifier.
     *
     * @param values Values to insert.
     * @return Auto incremental identifier.
     */
    protected Optional<Long> insert(@NonNull Map<String, Object> values) {
        return getSqlModifier().modify(Statements.insert(
                getTable(),
                values
        ));
    }

    /**
     * Fetches single record using given statement.
     *
     * @param stmt Statement.
     * @return Matched record, if any.
     */
    protected @NonNull Optional<Stored<I, T>> fetchOne(@NonNull StatementPrototype stmt) {
        return fetchEntity(stmt).map(Stored.Convertible::convert);
    }

    /**
     * Fetches single record using given statement.
     *
     * @param stmt Statement.
     * @return Matched record, if any.
     */
    protected @NonNull Optional<Stored<I, T>> fetchOne(@NonNull Statement stmt) {
        return fetchEntity(stmt).map(Stored.Convertible::convert);
    }

    /**
     * Fetches list of records.
     *
     * @param stmt Statement.
     * @return Collection of records.
     */
    protected @NonNull List<Stored<I, T>> fetchList(@NonNull StatementPrototype stmt) {
        return fetchEntityList(stmt).stream().map(Stored.Convertible::convert).collect(Collectors.toList());
    }

    /**
     * Fetches list of records.
     *
     * @param stmt Statement.
     * @return Collection of records.
     */
    protected @NonNull List<Stored<I, T>> fetchList(@NonNull Statement stmt) {
        return fetchEntityList(stmt).stream().map(Stored.Convertible::convert).collect(Collectors.toList());
    }
}
