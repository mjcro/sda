package io.github.mjcro.sda.spring;

import com.mysql.cj.jdbc.MysqlDataSource;
import io.github.mjcro.sda.Dialect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.sql.DataSource;
import java.util.function.Supplier;

@Configuration
@Import(SimpleDatabaseAccessConfiguration.class)
public class MySqlDatabaseAccessConfiguration {
    @Bean
    public Supplier<Dialect> dialectSupplier() {
        return () -> Dialect.MySQL;
    }

    @Bean
    public DataSource dataSource(
            @Value("${mysql.user:root}") String user,
            @Value("${mysql.password:root}") String password,
            @Value("${mysql.server:127.0.0.1}") String server,
            @Value("${mysql.port:3306}") int port,
            @Value("${mysql.database}") String database
    ) {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUser(user);
        dataSource.setPassword(password);
        dataSource.setServerName(server);
        dataSource.setPort(port);
        dataSource.setDatabaseName(database);

        return dataSource;
    }
}
