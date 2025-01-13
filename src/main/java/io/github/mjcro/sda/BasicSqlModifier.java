package io.github.mjcro.sda;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.OptionalLong;

/**
 * Basic implementation of {@link SqlModifier}.
 */
public class BasicSqlModifier extends BasicSqlInvoker implements SqlModifier {
    /**
     * Main constructor.
     *
     * @param dialect            Dialect, supported by this invoker.
     * @param connectionProvider Connection provider to use.
     * @param rowMapperFactory   Row mapper used to map rows to Java objects.
     * @param sqlTracer          Component used to track SQL timings and errors. Optional.
     */
    public BasicSqlModifier(
            Dialect dialect,
            ConnectionProvider connectionProvider,
            RowMapperFactory rowMapperFactory,
            SqlTracer sqlTracer
    ) {
        super(dialect, connectionProvider, rowMapperFactory, sqlTracer);
    }

    @Override
    public OptionalLong modify(String sql, Object[] parameters) {
        long nano = System.nanoTime();
        try (Connection connection = getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {
                prepareAndSet(preparedStatement, parameters);

                int affected = preparedStatement.executeUpdate();
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys != null && generatedKeys.isBeforeFirst()) {
                        generatedKeys.next();
                        finishSqlTraceNano(sql, parameters, nano, null);
                        return OptionalLong.of(generatedKeys.getLong(1));
                    }
                }
                finishSqlTraceNano(sql, parameters, nano, null);
                return OptionalLong.of(affected);
            }
        } catch (SQLException e) {
            DatabaseException converted = getDialect().convertException(e);
            finishSqlTraceNano(sql, parameters, nano, converted);
            throw converted;
        }
    }
}
