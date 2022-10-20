package io.github.mjcro.sda;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.OptionalLong;

public class BasicSqlModifier extends BasicSqlInvoker implements SqlModifier {
    public BasicSqlModifier(Dialect dialect, ConnectionProvider connectionProvider, RowMapperFactory rowMapperFactory, PlaceholderMapper placeholderMapper) {
        super(dialect, connectionProvider, rowMapperFactory, placeholderMapper);
    }

    @Override
    public OptionalLong modify(String sql, Object[] placeholders) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {
                prepareAndSet(preparedStatement, placeholders);

                int affected = preparedStatement.executeUpdate();
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys != null && generatedKeys.isBeforeFirst()) {
                        generatedKeys.next();
                        return OptionalLong.of(generatedKeys.getLong(1));
                    }
                }
                return OptionalLong.of(affected);
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }
}
