package io.github.mjcro.sda;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BasicSqlInvoker implements SqlInvoker {
    private final Dialect dialect;
    private final ConnectionProvider connectionProvider;
    private final RowMapperFactory rowMapperFactory;
    private final PlaceholderMapper placeholderMapper;

    public BasicSqlInvoker(
            Dialect dialect,
            ConnectionProvider connectionProvider,
            RowMapperFactory rowMapperFactory,
            PlaceholderMapper placeholderMapper
    ) {
        this.dialect = dialect;
        this.connectionProvider = connectionProvider;
        this.rowMapperFactory = rowMapperFactory;
        this.placeholderMapper = placeholderMapper;
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
                statement.setObject(i + 1, placeholderMapper.map(placeholders[i]));
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> list(Class<? extends T> clazz, String sql, Object[] placeholders) {
        RowMapper<T> rowMapper = (RowMapper<T>) rowMapperFactory.get(clazz);

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

                    return list;
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }
}
