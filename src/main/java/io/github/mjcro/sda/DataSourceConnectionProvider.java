package io.github.mjcro.sda;

import io.github.mjcro.interfaces.sql.ConnectionProvider;
import org.jspecify.annotations.NonNull;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

/**
 * Adapts a JDBC {@link DataSource} to the {@link ConnectionProvider} contract.
 */
public class DataSourceConnectionProvider implements ConnectionProvider {
    protected final DataSource dataSource;

    /**
     * @param dataSource Underlying JDBC data source. Not null.
     */
    public DataSourceConnectionProvider(@NonNull DataSource dataSource) {
        this.dataSource = Objects.requireNonNull(dataSource, "dataSource");
    }

    @Override
    public final @NonNull Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
