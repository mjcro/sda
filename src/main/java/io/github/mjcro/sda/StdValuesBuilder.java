package io.github.mjcro.sda;

import io.github.mjcro.interfaces.StrongType;
import io.github.mjcro.interfaces.ints.StrongInt;
import io.github.mjcro.interfaces.longs.StrongLong;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class StdValuesBuilder<T extends StdValuesBuilder<T>> implements ValuesBuilder<T> {
    private final HashMap<String, Object> values = new HashMap<>();

    @Override
    public T put(@NonNull String key, @Nullable Object value) {
        if (value instanceof Optional<?>) {
            // Unfolding Optional
            return put(key, ((Optional<?>) value).orElse(null));
        } else if (value instanceof StrongLong) {
            // Unfolding strong long types
            return put(key, ((StrongLong) value).value());
        } else if (value instanceof StrongInt) {
            // Unfolding strong integer types
            return put(key, ((StrongInt) value).value());
        } else if (value instanceof StrongType<?>) {
            // Unfolding strong types
            return put(key, ((StrongType<?>) value).value());
        }

        values.put(key, value);
        return self();
    }

    /**
     * Puts key-value pair only if condition is met.
     *
     * @param key       Key.
     * @param value     Value.
     * @param condition Condition.
     * @return Self.
     */
    public T putIf(@NonNull String key, @Nullable Object value, boolean condition) {
        if (condition) {
            return put(key, value);
        }
        return self();
    }

    /**
     * Puts key-value pair only if condition is met.
     *
     * @param key       Key.
     * @param value     Value.
     * @param condition Condition.
     * @return Self.
     */
    public T putIf(@NonNull String key, @Nullable Object value, @NonNull Supplier<Boolean> condition) {
        if (condition.get()) {
            return put(key, value);
        }
        return self();
    }

    /**
     * Puts key-value pair only if condition is met.
     *
     * @param key       Key.
     * @param value     Value.
     * @param condition Condition.
     * @return Self.
     */
    public <V> T putIf(@NonNull String key, @NonNull V value, @NonNull Predicate<V> condition) {
        if (condition.test(value)) {
            return put(key, value);
        }
        return self();
    }

    @SuppressWarnings("unchecked")
    @Override
    public T self() {
        return (T) this;
    }

    @Override
    public Map<String, Object> build() {
        return values;
    }
}
