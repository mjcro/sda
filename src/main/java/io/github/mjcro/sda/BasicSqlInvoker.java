package io.github.mjcro.sda;

import io.github.mjcro.interfaces.sql.ConnectionProvider;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

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
public class BasicSqlInvoker implements SqlInvoker, SourceWither<SqlInvoker> {
    private final Object source;
    private final Dialect dialect;
    protected final ConnectionProvider connectionProvider;
    protected final RowMapperFactory rowMapperFactory;
    protected final SqlTracer sqlTracer;

    /**
     * Constructor.
     *
     * @param dialect            Dialect, supported by this invoker.
     * @param connectionProvider Connection provider to use.
     * @param rowMapperFactory   Row mapper used to map rows to Java objects.
     * @param sqlTracer          Component used to track SQL timings and errors. Optional.
     */
    public BasicSqlInvoker(
            @NonNull Dialect dialect,
            @NonNull ConnectionProvider connectionProvider,
            @NonNull RowMapperFactory rowMapperFactory,
            @Nullable SqlTracer sqlTracer
    ) {
        this(null, dialect, connectionProvider, rowMapperFactory, sqlTracer);
    }

    /**
     * Constructor.
     *
     * @param source             Trace source, repository object in most cases.
     * @param dialect            Dialect, supported by this invoker.
     * @param connectionProvider Connection provider to use.
     * @param rowMapperFactory   Row mapper used to map rows to Java objects.
     * @param sqlTracer          Component used to track SQL timings and errors. Optional.
     */
    BasicSqlInvoker(
            @Nullable Object source,
            @NonNull Dialect dialect,
            @NonNull ConnectionProvider connectionProvider,
            @NonNull RowMapperFactory rowMapperFactory,
            @Nullable SqlTracer sqlTracer
    ) {
        this.dialect = Objects.requireNonNull(dialect, "dialect");
        this.source = source == null ? this : source;
        this.connectionProvider = Objects.requireNonNull(connectionProvider, "connectionProvider");
        this.rowMapperFactory = Objects.requireNonNull(rowMapperFactory, "rowMapperFactory");
        this.sqlTracer = sqlTracer == null
                ? (src, sql, parameters, elapsed, error) -> {
        }
                : sqlTracer;
    }


    @Override
    public @NonNull Dialect getDialect() {
        return dialect;
    }

    @Override
    public @NonNull BasicSqlInvoker withSource(@NonNull Object source) {
        return this.source == source
                ? this
                : new BasicSqlInvoker(source, dialect, connectionProvider, rowMapperFactory, sqlTracer);
    }

    /**
     * Constructs connection instance to use.
     *
     * @return Database connection.
     * @throws SQLException On connection error.
     */
    protected @NonNull Connection getConnection() throws SQLException {
        return connectionProvider.getConnection();
    }

    /**
     * Prepares and sets parameters values to given statement.
     *
     * @param statement  Statement to set data into.
     * @param parameters Values to set.
     * @throws SQLException On statement error.
     */
    protected void prepareAndSet(@NonNull PreparedStatement statement, @Nullable Object @Nullable [] parameters) throws SQLException {
        if (parameters != null && parameters.length > 0) {
            for (int i = 0; i < parameters.length; i++) {
                statement.setObject(i + 1, getDialect().prepareParameters(parameters[i]));
            }
        }
    }

    /**
     * Tracks finish of SQL query invocation.
     *
     * @param sql        SQL query.
     * @param parameters Parameters.
     * @param startNano  Nano time when query processing started.
     * @param error      Exception, optional.
     */
    protected void finishSqlTraceNano(
            @NonNull String sql,
            @Nullable Object @Nullable [] parameters,
            long startNano,
            @Nullable Exception error
    ) {
        sqlTracer.trace(source, sql, parameters, Duration.ofNanos(System.nanoTime() - startNano), error);
    }

    @SuppressWarnings("unchecked")
    @Override
    public @NonNull <T> List<T> list(@NonNull Class<? extends T> clazz, @NonNull String sql, @Nullable Object @Nullable [] parameters) {
        RowMapper<T> rowMapper = (RowMapper<T>) rowMapperFactory.get(clazz);

        long nano = System.nanoTime();
        try (Connection connection = getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                prepareAndSet(preparedStatement, parameters);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (!resultSet.isBeforeFirst()) {
                        finishSqlTraceNano(sql, parameters, nano, null);
                        return List.of(); // No data
                    }
                    ArrayList<T> list = new ArrayList<>();
                    while (resultSet.next()) {
                        if (resultSet.isAfterLast()) {
                            break;
                        }

                        list.add(rowMapper.mapRow(resultSet));
                    }

                    finishSqlTraceNano(sql, parameters, nano, null);
                    return list;
                }
            }
        } catch (SQLException e) {
            DatabaseException converted = getDialect().convertException(e);
            finishSqlTraceNano(sql, parameters, nano, converted);
            throw converted;
        }
    }
}
