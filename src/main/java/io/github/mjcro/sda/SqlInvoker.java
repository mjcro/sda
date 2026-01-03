package io.github.mjcro.sda;

import io.github.mjcro.interfaces.database.Statement;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Defines components able to perform read-only operations on database
 * with further data mapping into Java classes.
 */
public interface SqlInvoker {
    /**
     * @return SQL invoker dialect to use.
     */
    @NonNull Dialect getDialect();

    /**
     * Fetches collection of items from database.
     * Never returns null, if no data - returns empty collection.
     *
     * @param clazz      Response item class.
     * @param sql        Query.
     * @param parameters Parameters for query.
     * @param <T>        Response item type.
     * @return Collection of items.
     */
    @NonNull <T> List<T> list(@NonNull Class<? extends T> clazz, @NonNull String sql, @Nullable Object @Nullable [] parameters);

    /**
     * Fetches collection of items from database.
     * Never returns null, if no data - returns empty collection.
     *
     * @param clazz     Response item class.
     * @param statement Statement containing query and parameters.
     * @param <T>       Response item type.
     * @return Collection of items.
     */
    default @NonNull <T> List<T> list(@NonNull Class<? extends T> clazz, @NonNull Statement statement) {
        return this.list(clazz, statement.getSql(), statement.getParameters());
    }

    /**
     * Fetches collection of items from database.
     * Never returns null, if no data - returns empty collection.
     *
     * @param clazz     Response item class.
     * @param prototype Statement containing query and parameters.
     * @param <T>       Response item type.
     * @return Collection of items.
     */
    default @NonNull <T> List<T> list(@NonNull Class<? extends T> clazz, @NonNull StatementPrototype prototype) {
        return this.list(clazz, prototype.createStatement(getDialect()));
    }

    /**
     * Fetches map of values from database.
     * Never returns null, if no data - returns empty map.
     *
     * @param clazz      Response item class.
     * @param keyMapper  Mapper to extract key from object.
     * @param sql        Query.
     * @param parameters Parameters for query.
     * @param <K>        Map key type.
     * @param <T>        Response item type.
     * @return Map of items.
     */
    default @NonNull <K, T> Map<K, T> map(
            @NonNull Class<? extends T> clazz,
            @NonNull Function<? super T, ? extends K> keyMapper,
            @NonNull String sql,
            @Nullable Object @Nullable [] parameters
    ) {
        return this.list(clazz, sql, parameters)
                .stream()
                .collect(Collectors.toMap(
                        keyMapper,
                        Function.identity()
                ));
    }

    /**
     * Fetches map of values from database.
     * Never returns null, if no data - returns empty map.
     *
     * @param clazz     Response item class.
     * @param keyMapper Mapper to extract key from object.
     * @param statement Statement containing query and parameters.
     * @param <K>       Map key type.
     * @param <T>       Response item type.
     * @return Map of items.
     */
    default @NonNull <K, T> Map<K, T> map(
            @NonNull Class<? extends T> clazz,
            @NonNull Function<? super T, ? extends K> keyMapper,
            @NonNull Statement statement
    ) {
        return this.map(clazz, keyMapper, statement.getSql(), statement.getParameters());
    }

    /**
     * Fetches map of values from database.
     * Never returns null, if no data - returns empty map.
     *
     * @param clazz     Response item class.
     * @param keyMapper Mapper to extract key from object.
     * @param prototype Statement containing query and parameters.
     * @param <K>       Map key type.
     * @param <T>       Response item type.
     * @return Map of items.
     */
    default @NonNull <K, T> Map<K, T> map(
            @NonNull Class<? extends T> clazz,
            @NonNull Function<? super T, ? extends K> keyMapper,
            @NonNull StatementPrototype prototype
    ) {
        return this.map(clazz, keyMapper, prototype.createStatement(getDialect()));
    }

    /**
     * Fetches grouped values from database.
     * Never returns null, if no data - returns empty map.
     *
     * @param clazz             Response item class.
     * @param groupingKeyMapper Mapper to extract grouping key from object.
     * @param sql               Query.
     * @param parameters        Parameters for query.
     * @param <K>               Map key type.
     * @param <T>               Response item type.
     * @return Map of items.
     */
    default @NonNull <K, T> Map<K, List<T>> grouped(
            @NonNull Class<? extends T> clazz,
            @NonNull Function<? super T, ? extends K> groupingKeyMapper,
            @NonNull String sql,
            @Nullable Object @Nullable [] parameters
    ) {
        return this.list(clazz, sql, parameters)
                .stream()
                .collect(Collectors.groupingBy(groupingKeyMapper));
    }

    /**
     * Fetches grouped values from database.
     * Never returns null, if no data - returns empty map.
     *
     * @param clazz             Response item class.
     * @param groupingKeyMapper Mapper to extract grouping key from object.
     * @param statement         Statement containing query and parameters.
     * @param <K>               Map key type.
     * @param <T>               Response item type.
     * @return Map of items.
     */
    default @NonNull <K, T> Map<K, List<T>> grouped(
            @NonNull Class<? extends T> clazz,
            @NonNull Function<? super T, ? extends K> groupingKeyMapper,
            @NonNull Statement statement
    ) {
        return this.grouped(clazz, groupingKeyMapper, statement.getSql(), statement.getParameters());
    }

    /**
     * Fetches grouped values from database.
     * Never returns null, if no data - returns empty map.
     *
     * @param clazz             Response item class.
     * @param groupingKeyMapper Mapper to extract grouping key from object.
     * @param prototype         Statement containing query and parameters.
     * @param <K>               Map key type.
     * @param <T>               Response item type.
     * @return Map of items.
     */
    default @NonNull <K, T> Map<K, List<T>> grouped(
            @NonNull Class<? extends T> clazz,
            @NonNull Function<? super T, ? extends K> groupingKeyMapper,
            @NonNull StatementPrototype prototype
    ) {
        return this.grouped(clazz, groupingKeyMapper, prototype.createStatement(getDialect()));
    }

    /**
     * Fetches single item from database.
     * Never returns null, if no data - returns empty optional.
     *
     * @param clazz      Response item class.
     * @param sql        Query.
     * @param parameters Parameters for query.
     * @param <T>        Response item type.
     * @return Item.
     */
    @SuppressWarnings("unchecked")
    default @NonNull <T> Optional<T> one(@NonNull Class<? extends T> clazz, @NonNull String sql, @Nullable Object @Nullable [] parameters) {
        return (Optional<T>) list(clazz, sql, parameters).stream().findFirst();
    }

    /**
     * Fetches single item from database.
     * Never returns null, if no data - returns empty optional.
     *
     * @param clazz     Response item class.
     * @param statement Statement containing query and parameters.
     * @param <T>       Response item type.
     * @return Item.
     */
    default @NonNull <T> Optional<T> one(@NonNull Class<? extends T> clazz, @NonNull Statement statement) {
        return one(clazz, statement.getSql(), statement.getParameters());
    }

    /**
     * Fetches single item from database.
     * Never returns null, if no data - returns empty optional.
     *
     * @param clazz     Response item class.
     * @param prototype Statement containing query and parameters.
     * @param <T>       Response item type.
     * @return Item.
     */
    default @NonNull <T> Optional<T> one(@NonNull Class<? extends T> clazz, @NonNull StatementPrototype prototype) {
        Statement statement = prototype.createStatement(getDialect());
        return one(clazz, statement.getSql(), statement.getParameters());
    }
}
