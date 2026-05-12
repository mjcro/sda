package io.github.mjcro.sda.prefab.stored;

import io.github.mjcro.state.State;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.time.Instant;
import java.util.Objects;

/**
 * Represents domain object stored in database.
 *
 * @param <I> Identifier type.
 * @param <T> Domain object type.
 */
public interface Stored<I, T> {
    /**
     * @return Identifier.
     */
    @NonNull I id();

    /**
     * @return Domain object.
     */
    @NonNull T value();

    /**
     * @return State.
     */
    @NonNull State state();

    /**
     * @return Creation time.
     */
    @NonNull Instant createdAt();

    /**
     * @return Update time.
     */
    @NonNull Instant updatedAt();

    /**
     * Factory constructing a {@link Stored} instance.
     *
     * @param id        Identifier. Not null.
     * @param state     Lifecycle state. Not null.
     * @param createdAt Creation time. Not null.
     * @param updatedAt Last update time. Null falls back to {@code createdAt}.
     * @param value     Domain value. Not null.
     * @param <I>       Identifier type.
     * @param <T>       Domain object type.
     * @return Stored instance, not null.
     */
    static <I, T> Stored<I, T> of(
            @NonNull I id,
            @NonNull State state,
            @NonNull Instant createdAt,
            @Nullable Instant updatedAt,
            @NonNull T value
    ) {
        return new StoredImpl<>(
                Objects.requireNonNull(id, "id"),
                Objects.requireNonNull(state, "state"),
                Objects.requireNonNull(createdAt, "createdAt"),
                updatedAt == null ? createdAt : updatedAt,
                Objects.requireNonNull(value, "value")
        );
    }

    /**
     * Factory constructing a {@link Stored} instance where update time equals creation time.
     *
     * @param id        Identifier. Not null.
     * @param state     Lifecycle state. Not null.
     * @param createdAt Creation time. Not null.
     * @param value     Domain value. Not null.
     * @param <I>       Identifier type.
     * @param <T>       Domain object type.
     * @return Stored instance, not null.
     */
    static <I, T> Stored<I, T> of(
            @NonNull I id,
            @NonNull State state,
            @NonNull Instant createdAt,
            @NonNull T value
    ) {
        return of(id, state, createdAt, createdAt, value);
    }

    /**
     * Defines entities able to be converted into {@link Stored}.
     *
     * @param <I> Id type.
     * @param <T> Domain type.
     */
    interface Convertible<I, T> {
        /**
         * Converts this object into its {@link Stored} representation.
         *
         * @return Stored representation, not null.
         */
        @NonNull Stored<I, T> convert();
    }
}
