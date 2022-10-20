package io.github.mjcro.sda;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionProvider {
    /**
     * Constructs new connection provider from given data source.
     *
     * @param dataSource Data source.
     * @return Connection provider.
     */
    static ConnectionProvider from(DataSource dataSource) {
        return new DataSourceConnectionProvider(dataSource);
    }

    /**
     * @return Database connection to use.
     */
    Connection getConnection() throws SQLException;
}
