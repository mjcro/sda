package io.github.mjcro.sda;

import io.github.mjcro.interfaces.sql.ConnectionProvider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.OptionalLong;

/**
 * Basic implementation of {@link SqlModifier}.
 */
public class BasicSqlModifier implements SqlModifier, SourceWither<SqlModifier> {
    private final BasicSqlInvoker basicSqlInvoker;

    /**
     * Constructor.
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
        this(new BasicSqlInvoker(dialect, connectionProvider, rowMapperFactory, sqlTracer));
    }

    /**
     * Constructor.
     *
     * @param invoker SQL invoker to wrap.
     */
    private BasicSqlModifier(BasicSqlInvoker invoker) {
        this.basicSqlInvoker = invoker;
    }

    @Override
    public Dialect getDialect() {
        return basicSqlInvoker.getDialect();
    }

    @Override
    public SqlModifier withSource(Object source) {
        return new BasicSqlModifier(basicSqlInvoker.withSource(source));
    }

    @Override
    public <T> List<T> list(Class<? extends T> clazz, String sql, Object[] parameters) {
        return basicSqlInvoker.list(clazz, sql, parameters);
    }

    @Override
    public OptionalLong modify(String sql, Object[] parameters) {
        long nano = System.nanoTime();
        try (Connection connection = basicSqlInvoker.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {
                basicSqlInvoker.prepareAndSet(preparedStatement, parameters);

                int affected = preparedStatement.executeUpdate();
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys != null && generatedKeys.isBeforeFirst()) {
                        generatedKeys.next();
                        basicSqlInvoker.finishSqlTraceNano(sql, parameters, nano, null);
                        return OptionalLong.of(generatedKeys.getLong(1));
                    }
                }
                basicSqlInvoker.finishSqlTraceNano(sql, parameters, nano, null);
                return OptionalLong.of(affected);
            }
        } catch (SQLException e) {
            DatabaseException converted = getDialect().convertException(e);
            basicSqlInvoker.finishSqlTraceNano(sql, parameters, nano, converted);
            throw converted;
        }
    }
}
