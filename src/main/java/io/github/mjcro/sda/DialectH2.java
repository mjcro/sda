package io.github.mjcro.sda;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

class DialectH2 implements Dialect {
    @Override
    public @NonNull String getName() {
        return "H2";
    }

    @Override
    public boolean isCompatibleWith(@Nullable Dialect other) {
        return other == Dialect.H2 || other == Dialect.MySQL;
    }
}
