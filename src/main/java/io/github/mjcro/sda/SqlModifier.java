package io.github.mjcro.sda;

import java.util.OptionalLong;

public interface SqlModifier extends SqlInvoker {
    /**
     * Performs modification request.
     *
     * @param sql          SQL.
     * @param placeholders Placeholders.
     * @return Identifier of inserted data or number of affected rows.
     */
    OptionalLong modify(String sql, Object[] placeholders);

    /**
     * Performs modification request.
     *
     * @param prototype Statement containing query and placeholders.
     * @return Identifier of inserted data or number of affected rows.
     */
    default OptionalLong modify(StatementPrototype prototype) {
        Statement statement = prototype.createStatement(getDialect());
        return modify(statement.getSql(), statement.getPlaceholders());
    }
}
