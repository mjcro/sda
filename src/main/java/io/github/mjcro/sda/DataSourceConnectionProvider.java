package io.github.mjcro.sda;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

public class DataSourceConnectionProvider implements ConnectionProvider {
    protected final DataSource dataSource;

    public DataSourceConnectionProvider(DataSource dataSource) {
        this.dataSource = Objects.requireNonNull(dataSource);
    }

    @Override
    public final Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
