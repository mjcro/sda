package io.github.mjcro.sda.reflection;

import org.jspecify.annotations.NonNull;

import java.sql.SQLException;

public class ScalarNullException extends SQLException {
    public ScalarNullException(@NonNull String type) {
        super("Got NULL for scalar " + type);
    }
}
