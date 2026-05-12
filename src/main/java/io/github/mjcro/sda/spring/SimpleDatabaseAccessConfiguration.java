package io.github.mjcro.sda.spring;

import io.github.mjcro.interfaces.sql.ConnectionProvider;
import io.github.mjcro.sda.BasicSqlInvoker;
import io.github.mjcro.sda.BasicSqlModifier;
import io.github.mjcro.sda.CommonClassesRowMapperFactoryAdapter;
import io.github.mjcro.sda.DataSourceConnectionProvider;
import io.github.mjcro.sda.Dialect;
import io.github.mjcro.sda.RowMapperFactory;
import io.github.mjcro.sda.SqlInvoker;
import io.github.mjcro.sda.SqlModifier;
import io.github.mjcro.sda.SqlTracer;
import io.github.mjcro.sda.TieredTypeHandlerList;
import io.github.mjcro.sda.reflection.BigDecimalTypeHandler;
import io.github.mjcro.sda.reflection.ByteArrayTypeHandler;
import io.github.mjcro.sda.reflection.ByteTypeHandler;
import io.github.mjcro.sda.reflection.CreatorAnnotationTypeHandler;
import io.github.mjcro.sda.reflection.DoubleTypeHandler;
import io.github.mjcro.sda.reflection.EntityConstructorTypeHandler;
import io.github.mjcro.sda.reflection.EntityFieldsTypeHandler;
import io.github.mjcro.sda.reflection.EnumIntegerIdTypeHandler;
import io.github.mjcro.sda.reflection.EnumLongIdTypeHandler;
import io.github.mjcro.sda.reflection.EnumNameTypeHandler;
import io.github.mjcro.sda.reflection.FloatTypeHandler;
import io.github.mjcro.sda.reflection.IntTypeHandler;
import io.github.mjcro.sda.reflection.LocalDateTypeHandler;
import io.github.mjcro.sda.reflection.LongTypeHandler;
import io.github.mjcro.sda.reflection.MapperAnnotationTypeHandler;
import io.github.mjcro.sda.reflection.ShortTypeHandler;
import io.github.mjcro.sda.reflection.StringTypeHandler;
import io.github.mjcro.sda.reflection.StrongTypesTypeHandler;
import io.github.mjcro.sda.reflection.TypeHandlerRowMapperFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.sql.DataSource;
import java.util.function.Supplier;

/**
 * Spring configuration wiring the dialect-agnostic part of the database stack:
 * {@link SqlInvoker}, {@link SqlModifier}, {@link ConnectionProvider} and a no-op {@link SqlTracer}.
 * A {@code DataSource} and {@code Supplier<Dialect>} must be provided externally.
 */
@Configuration
@Import(RowMapperFactoryConfiguration.class)
public class SimpleDatabaseAccessConfiguration {
    @Bean
    public SqlModifier sqlModifier(
            RowMapperFactory factory,
            ConnectionProvider connectionProvider,
            Supplier<Dialect> dialectSupplier,
            SqlTracer sqlTracer
    ) {
        return new BasicSqlModifier(dialectSupplier.get(), connectionProvider, factory, sqlTracer);
    }

    @Bean
    public SqlInvoker sqlInvoker(
            RowMapperFactory factory,
            ConnectionProvider connectionProvider,
            Supplier<Dialect> dialectSupplier,
            SqlTracer sqlTracer
    ) {
        return new BasicSqlInvoker(dialectSupplier.get(), connectionProvider, factory, sqlTracer);
    }

    @Bean
    public SqlTracer sqlTracer() {
        return (source, sql, parameters, elapsed, error) -> {
        };
    }

    @Bean
    public ConnectionProvider connectionProvider(DataSource dataSource) {
        return new DataSourceConnectionProvider(dataSource);
    }
}
