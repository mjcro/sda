package io.github.mjcro.sda.prefab.stored;

import io.github.mjcro.state.State;
import org.jspecify.annotations.NonNull;

import java.time.Instant;

class StoredImpl<I, T> implements Stored<I, T> {
    private final I id;
    private final State state;
    private final Instant createdAt, updatedAt;
    private final T value;

    StoredImpl(I id, State state, Instant createdAt, Instant updatedAt, T value) {
        this.id = id;
        this.state = state;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.value = value;
    }

    @Override
    public @NonNull I id() {
        return id;
    }

    @Override
    public @NonNull T value() {
        return value;
    }

    @Override
    public @NonNull State state() {
        return state;
    }

    @Override
    public @NonNull Instant createdAt() {
        return createdAt;
    }

    @Override
    public @NonNull Instant updatedAt() {
        return updatedAt;
    }

    @Override
    public String toString() {
        return state() + " " + id() + " " + value();
    }
}
