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
import io.github.mjcro.sda.reflection.LongTypeHandler;
import io.github.mjcro.sda.reflection.MapperAnnotationTypeHandler;
import io.github.mjcro.sda.reflection.ShortTypeHandler;
import io.github.mjcro.sda.reflection.StringTypeHandler;
import io.github.mjcro.sda.reflection.TypeHandlerRowMapperFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.function.Supplier;

@Configuration
public class SimpleDatabaseAccessConfiguration {
    @Bean
    public TieredTypeHandlerList tieredTypeHandlerList() {
        TieredTypeHandlerList list = new TieredTypeHandlerList();
            return list
                    .addLast(
                            TieredTypeHandlerList.Tier.ENTITY_CONSTRUCTORS,
                            new EntityFieldsTypeHandler(list),
                            new EntityConstructorTypeHandler(list)
                    )
                    .addLast(
                            TieredTypeHandlerList.Tier.ANNOTATION_PROCESSORS,
                            new MapperAnnotationTypeHandler(),
                            new CreatorAnnotationTypeHandler()
                    )
                    .addLast(
                            TieredTypeHandlerList.Tier.TYPES,
                            new StringTypeHandler(),
                            new ByteTypeHandler(false),
                            new ByteTypeHandler(true),
                            new ShortTypeHandler(false),
                            new ShortTypeHandler(true),
                            new IntTypeHandler(false),
                            new IntTypeHandler(true),
                            new LongTypeHandler(false),
                            new LongTypeHandler(true),
                            new FloatTypeHandler(false),
                            new FloatTypeHandler(true),
                            new DoubleTypeHandler(false),
                            new DoubleTypeHandler(true),
                            new BigDecimalTypeHandler(),
                            new ByteArrayTypeHandler(),
                            new EnumIntegerIdTypeHandler(),
                            new EnumLongIdTypeHandler(),
                            new EnumNameTypeHandler()
                    );
    }

    @Bean
    public RowMapperFactory rowMapperFactory(TieredTypeHandlerList tieredTypeHandlerList) {
        return new CommonClassesRowMapperFactoryAdapter(
                new TypeHandlerRowMapperFactory(tieredTypeHandlerList)
        );
    }

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
