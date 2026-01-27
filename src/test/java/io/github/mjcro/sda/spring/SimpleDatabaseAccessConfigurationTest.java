package io.github.mjcro.sda.spring;

import io.github.mjcro.interfaces.StrongType;
import io.github.mjcro.interfaces.ints.StrongInt;
import io.github.mjcro.interfaces.longs.StrongLong;
import io.github.mjcro.interfaces.sql.ConnectionProvider;
import io.github.mjcro.sda.Dialect;
import io.github.mjcro.sda.TieredTypeHandlerList;
import io.github.mjcro.sda.VirtualColumn;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.function.Supplier;
import java.util.stream.Stream;

class SimpleDatabaseAccessConfigurationTest {
    private final ApplicationContext context = new AnnotationConfigApplicationContext(TestConfiguration.class);

    static Stream<Arguments> supportsDataProvider() {
        return Stream.of(
                Arguments.of(String.class),
                Arguments.of(byte.class),
                Arguments.of(Byte.class),
                Arguments.of(short.class),
                Arguments.of(Short.class),
                Arguments.of(int.class),
                Arguments.of(Integer.class),
                Arguments.of(long.class),
                Arguments.of(Long.class),
                Arguments.of(float.class),
                Arguments.of(Float.class),
                Arguments.of(double.class),
                Arguments.of(Double.class),
                Arguments.of(BigDecimal.class),
                Arguments.of(byte[].class),
                Arguments.of(LocalDate.class),
                Arguments.of(StrongLongType.class),
                Arguments.of(StrongIntType.class),
                Arguments.of(StrongStringType.class)
        );
    }

    @ParameterizedTest
    @MethodSource("supportsDataProvider")
    void testSupports(Class<?> clazz) {
        TieredTypeHandlerList bean = context.getBean(TieredTypeHandlerList.class);
        Assertions.assertTrue(bean.supports(new VirtualColumn<>("", clazz)));
    }

    static Stream<Arguments> notSupportsDataProvider() {
        return Stream.of(
                Arguments.of(boolean.class),
                Arguments.of(Instant.class),
                Arguments.of(Duration.class),
                Arguments.of(StrongInstantType.class) // Only StrongType<String> supported
        );
    }

    @ParameterizedTest
    @MethodSource("notSupportsDataProvider")
    void testNotSupports(Class<?> clazz) {
        TieredTypeHandlerList bean = context.getBean(TieredTypeHandlerList.class);
        Assertions.assertFalse(bean.supports(new VirtualColumn<>("", clazz)));
    }

    @Configuration
    @Import(SimpleDatabaseAccessConfiguration.class)
    public static class TestConfiguration {
        @Bean
        public Supplier<Dialect> dialectSupplier() {
            return () -> Dialect.H2;
        }

        @Bean
        public ConnectionProvider connectionProvider() {
            return () -> null;
        }
    }

    private static class StrongLongType implements StrongLong {
        private final long x;

        private StrongLongType(long x) {
            this.x = x;
        }

        @Override
        public long value() {
            return x;
        }
    }

    private static class StrongIntType implements StrongInt {
        private final int x;

        private StrongIntType(int x) {
            this.x = x;
        }

        @Override
        public int value() {
            return x;
        }
    }

    private static class StrongStringType implements StrongType<String> {
        private final String x;

        private StrongStringType(String x) {
            this.x = x;
        }

        @Override
        public String value() {
            return x;
        }
    }

    private static class StrongInstantType implements StrongType<Instant> {
        private final Instant x;

        private StrongInstantType(Instant x) {
            this.x = x;
        }

        @Override
        public Instant value() {
            return x;
        }
    }
}