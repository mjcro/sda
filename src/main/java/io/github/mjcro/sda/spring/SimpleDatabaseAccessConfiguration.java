package io.github.mjcro.sda.spring;

import io.github.mjcro.sda.BasicSqlInvoker;
import io.github.mjcro.sda.BasicSqlModifier;
import io.github.mjcro.sda.CommonClassesRowMapperFactoryAdapter;
import io.github.mjcro.sda.ConnectionProvider;
import io.github.mjcro.sda.DataSourceConnectionProvider;
import io.github.mjcro.sda.Dialect;
import io.github.mjcro.sda.RowMapperFactory;
import io.github.mjcro.sda.SqlInvoker;
import io.github.mjcro.sda.SqlModifier;
import io.github.mjcro.sda.reflection.ReflectiveAutoRegistrationRowMapperFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.function.Supplier;

@Configuration
public class SimpleDatabaseAccessConfiguration {
    @Bean
    public RowMapperFactory rowMapperFactory() {
        return new CommonClassesRowMapperFactoryAdapter(
                ReflectiveAutoRegistrationRowMapperFactory.standard()
        );
    }

    @Bean
    public SqlModifier sqlModifier(
            RowMapperFactory factory,
            ConnectionProvider connectionProvider,
            Supplier<Dialect> dialectSupplier
    ) {
        return new BasicSqlModifier(dialectSupplier.get(), connectionProvider, factory, $ -> $);
    }

    @Bean
    public SqlInvoker sqlInvoker(
            RowMapperFactory factory,
            ConnectionProvider connectionProvider,
            Supplier<Dialect> dialectSupplier
    ) {
        return new BasicSqlInvoker(dialectSupplier.get(), connectionProvider, factory, $ -> $);
    }

    @Bean
    public ConnectionProvider connectionProvider(DataSource dataSource) {
        return new DataSourceConnectionProvider(dataSource);
    }
}
