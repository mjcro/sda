package io.github.mjcro.sda;

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
    Dialect getDialect();

    /**
     * Fetches collection of items from database.
     * Never returns null, if no data - returns empty collection.
     *
     * @param clazz        Response item class.
     * @param sql          Query.
     * @param placeholders Placeholders for query.
     * @param <T>          Response item type.
     * @return Collection of items.
     */
    <T> List<T> list(Class<? extends T> clazz, String sql, Object[] placeholders);

    /**
     * Fetches collection of items from database.
     * Never returns null, if no data - returns empty collection.
     *
     * @param clazz     Response item class.
     * @param prototype Statement containing query and placeholders.
     * @param <T>       Response item type.
     * @return Collection of items.
     */
    default <T> List<T> list(Class<? extends T> clazz, StatementPrototype prototype) {
        Statement statement = prototype.createStatement(getDialect());
        return this.list(clazz, statement.getSql(), statement.getPlaceholders());
    }

    /**
     * Fetches map of values from database.
     * Never returns null, if no data - returns empty map.
     *
     * @param clazz        Response item class.
     * @param keyMapper    Mapper to extract key from object.
     * @param sql          Query.
     * @param placeholders Placeholders for query.
     * @param <K>          Map key type.
     * @param <T>          Response item type.
     * @return Map of items.
     */
    default <K, T> Map<K, T> map(
            Class<? extends T> clazz,
            Function<? super T, ? extends K> keyMapper,
            String sql,
            Object[] placeholders
    ) {
        return this.list(clazz, sql, placeholders)
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
     * @param prototype Statement containing query and placeholders.
     * @param <K>       Map key type.
     * @param <T>       Response item type.
     * @return Map of items.
     */
    default <K, T> Map<K, T> map(
            Class<? extends T> clazz,
            Function<? super T, ? extends K> keyMapper,
            StatementPrototype prototype
    ) {
        Statement statement = prototype.createStatement(getDialect());
        return this.map(clazz, keyMapper, statement.getSql(), statement.getPlaceholders());
    }

    /**
     * Fetches single item from database.
     * Never returns null, if no data - returns empty optional.
     *
     * @param clazz        Response item class.
     * @param sql          Query.
     * @param placeholders Placeholders for query.
     * @param <T>          Response item type.
     * @return Item.
     */
    @SuppressWarnings("unchecked")
    default <T> Optional<T> one(Class<? extends T> clazz, String sql, Object[] placeholders) {
        return (Optional<T>) list(clazz, sql, placeholders).stream().findFirst();
    }

    /**
     * Fetches single item from database.
     * Never returns null, if no data - returns empty optional.
     *
     * @param clazz     Response item class.
     * @param prototype Statement containing query and placeholders.
     * @param <T>       Response item type.
     * @return Item.
     */
    default <T> Optional<T> one(Class<? extends T> clazz, StatementPrototype prototype) {
        Statement statement = prototype.createStatement(getDialect());
        return one(clazz, statement.getSql(), statement.getPlaceholders());
    }
}
