package io.github.mjcro.sda;

import io.github.mjcro.interfaces.sql.ConnectionProvider;
import org.jspecify.annotations.NonNull;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

public class DataSourceConnectionProvider implements ConnectionProvider {
    protected final DataSource dataSource;

    public DataSourceConnectionProvider(@NonNull DataSource dataSource) {
        this.dataSource = Objects.requireNonNull(dataSource, "dataSource");
    }

    @Override
    public final @NonNull Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
