package io.github.mjcro.sda;

import io.github.mjcro.interfaces.database.Statement;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.OptionalLong;

/**
 * Defines components able to perform read and write operations on database
 * with further data mapping into Java classes.
 */
public interface SqlModifier extends SqlInvoker {
    /**
     * Performs data modification request (INSERT, UPDATE, DELETE, etc...).
     *
     * @param sql        SQL.
     * @param parameters Parameters.
     * @return Identifier of inserted data or number of affected rows.
     */
    @NonNull OptionalLong modify(@NonNull String sql, @Nullable Object @Nullable [] parameters);

    /**
     * Performs modification request (INSERT, UPDATE, DELETE, etc...).
     *
     * @param prototype Statement containing query and parameters.
     * @return Identifier of inserted data or number of affected rows.
     */
    default @NonNull OptionalLong modify(@NonNull StatementPrototype prototype) {
        return modify(prototype.createStatement(getDialect()));
    }

    /**
     * Performs modification request (INSERT, UPDATE, DELETE, etc...).
     *
     * @param statement Statement containing query and parameters.
     * @return Identifier of inserted data or number of affected rows.
     */
    default @NonNull OptionalLong modify(@NonNull Statement statement) {
        return modify(statement.getSql(), statement.getParameters());
    }
}
