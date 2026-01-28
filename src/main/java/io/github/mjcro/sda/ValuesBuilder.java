package io.github.mjcro.sda;

import io.github.mjcro.interfaces.builders.SelfTyped;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Map;
import java.util.Optional;

/**
 * Helper to generate maps to be used in INSERT/UPDATE statements.
 */
public interface ValuesBuilder<T extends ValuesBuilder<T>> extends SelfTyped<T> {
    /**
     * Puts a pair of key and value into builder.
     *
     * @param key   Key.
     * @param value Value.
     * @return Self.
     */
    T put(@NonNull String key, @Nullable Object value);

    /**
     * @return Built objects.
     */
    Map<String, Object> build();

    /**
     * Performs INSERT operation using given {@link SqlModifier}.
     *
     * @param db    SQL modifier.
     * @param table Database table name.
     * @return Incremental identifier, if any.
     */
    default Optional<Long> insert(@NonNull SqlModifier db, @NonNull String table) {
        return db.modify(Statements.insert(table, build()));
    }
}
