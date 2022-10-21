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
     * @param placeholderMapper  Placeholder mapper used to map incoming placeholders before passing them
     *                           to database. Optional.
     * @param sqlTracer          Component used to track SQL timings and errors. Optional.
     */
    public BasicSqlModifier(
            Dialect dialect,
            ConnectionProvider connectionProvider,
            RowMapperFactory rowMapperFactory,
            PlaceholderMapper placeholderMapper,
            SqlTracer sqlTracer
    ) {
        super(dialect, connectionProvider, rowMapperFactory, placeholderMapper, sqlTracer);
    }

    @Override
    public OptionalLong modify(String sql, Object[] placeholders) {
        long nano = System.nanoTime();
        try (Connection connection = getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {
                prepareAndSet(preparedStatement, placeholders);

                int affected = preparedStatement.executeUpdate();
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys != null && generatedKeys.isBeforeFirst()) {
                        generatedKeys.next();
                        finishSqlTraceNano(sql, placeholders, nano, null);
                        return OptionalLong.of(generatedKeys.getLong(1));
                    }
                }
                finishSqlTraceNano(sql, placeholders, nano, null);
                return OptionalLong.of(affected);
            }
        } catch (SQLException e) {
            finishSqlTraceNano(sql, placeholders, nano, e);
            throw new DatabaseException(e);
        }
    }
}
