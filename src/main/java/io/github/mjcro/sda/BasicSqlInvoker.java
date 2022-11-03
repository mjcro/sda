package io.github.mjcro.sda;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Basic implementation of {@link SqlInvoker}.
 */
public class BasicSqlInvoker implements SqlInvoker {
    private final Dialect dialect;
    protected final ConnectionProvider connectionProvider;
    protected final RowMapperFactory rowMapperFactory;
    protected final SqlTracer sqlTracer;

    /**
     * Main constructor.
     *
     * @param dialect            Dialect, supported by this invoker.
     * @param connectionProvider Connection provider to use.
     * @param rowMapperFactory   Row mapper used to map rows to Java objects.
     * @param sqlTracer          Component used to track SQL timings and errors. Optional.
     */
    public BasicSqlInvoker(
            Dialect dialect,
            ConnectionProvider connectionProvider,
            RowMapperFactory rowMapperFactory,
            SqlTracer sqlTracer
    ) {
        this.dialect = Objects.requireNonNull(dialect, "dialect");
        this.connectionProvider = Objects.requireNonNull(connectionProvider, "connectionProvider");
        this.rowMapperFactory = Objects.requireNonNull(rowMapperFactory, "rowMapperFactory");
        this.sqlTracer = sqlTracer == null
                ? (sql, placeholders, elapsed, error) -> {
        }
                : sqlTracer;
    }

    @Override
    public Dialect getDialect() {
        return dialect;
    }

    /**
     * Constructs connection instance to use.
     *
     * @return Database connection.
     * @throws SQLException On connection error.
     */
    protected Connection getConnection() throws SQLException {
        return connectionProvider.getConnection();
    }

    /**
     * Prepares and sets placeholder values to given statement.
     *
     * @param statement    Statement to set data into.
     * @param placeholders Values to set.
     * @throws SQLException On statement error.
     */
    protected void prepareAndSet(PreparedStatement statement, Object[] placeholders) throws SQLException {
        if (placeholders != null && placeholders.length > 0) {
            for (int i = 0; i < placeholders.length; i++) {
                statement.setObject(i + 1, getDialect().preparePlaceholder(placeholders[i]));
            }
        }
    }

    /**
     * Tracks finish of SQL query invocation.
     *
     * @param sql          SQL query.
     * @param placeholders Placeholders.
     * @param startNano    Nano time when query processing started.
     * @param error        Exception, optional.
     */
    protected void finishSqlTraceNano(
            String sql,
            Object[] placeholders,
            long startNano,
            Exception error
    ) {
        sqlTracer.trace(sql, placeholders, Duration.ofNanos(System.nanoTime() - startNano), error);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> list(Class<? extends T> clazz, String sql, Object[] placeholders) {
        RowMapper<T> rowMapper = (RowMapper<T>) rowMapperFactory.get(clazz);

        long nano = System.nanoTime();
        try (Connection connection = getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                prepareAndSet(preparedStatement, placeholders);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (!resultSet.isBeforeFirst()) {
                        return List.of(); // No data
                    }
                    ArrayList<T> list = new ArrayList<>();
                    while (resultSet.next()) {
                        if (resultSet.isAfterLast()) {
                            break;
                        }

                        list.add(rowMapper.mapRow(resultSet));
                    }

                    finishSqlTraceNano(sql, placeholders, nano, null);
                    return list;
                }
            }
        } catch (SQLException e) {
            finishSqlTraceNano(sql, placeholders, nano, e);
            throw new DatabaseException(e);
        }
    }
}
