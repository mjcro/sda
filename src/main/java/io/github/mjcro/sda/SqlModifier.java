package io.github.mjcro.sda;

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
    OptionalLong modify(String sql, Object[] parameters);

    /**
     * Performs modification request (INSERT, UPDATE, DELETE, etc...).
     *
     * @param prototype Statement containing query and parameters.
     * @return Identifier of inserted data or number of affected rows.
     */
    default OptionalLong modify(StatementPrototype prototype) {
        Statement statement = prototype.createStatement(getDialect());
        return modify(statement.getSql(), statement.getParameters());
    }
}
