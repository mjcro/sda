package io.github.mjcro.sda.reflection;

import io.github.mjcro.sda.VirtualResultSet;
import io.github.mjcro.sda.reflection.AbstractSimpleColumnTypeHandler;
import io.github.mjcro.sda.reflection.BigDecimalTypeHandler;
import io.github.mjcro.sda.reflection.ByteTypeHandler;
import io.github.mjcro.sda.reflection.DoubleTypeHandler;
import io.github.mjcro.sda.reflection.FloatTypeHandler;
import io.github.mjcro.sda.reflection.IntTypeHandler;
import io.github.mjcro.sda.reflection.LongTypeHandler;
import io.github.mjcro.sda.reflection.ShortTypeHandler;
import io.github.mjcro.sda.reflection.StringBooleanTypeHandler;
import io.github.mjcro.sda.reflection.StringTypeHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.stream.Stream;

public class ColumnTypeHandlersTest {
    static Stream<Arguments> valueReaderDataProvider() {
        return Stream.of(
                Arguments.of("Foo", "Foo", String.class, new StringTypeHandler()),
                Arguments.of(null, null, String.class, new StringTypeHandler()),

                Arguments.of((byte) 11, (byte) 11, Byte.class, new ByteTypeHandler(true)),
                Arguments.of(null, null, Byte.class, new ByteTypeHandler(true)),
                Arguments.of((byte) -12, (byte) -12, byte.class, new ByteTypeHandler(false)),
                Arguments.of((short) 92, (short) 92, Short.class, new ShortTypeHandler(true)),
                Arguments.of(null, null, Short.class, new ShortTypeHandler(true)),
                Arguments.of((short) -12, (short) -12, short.class, new ShortTypeHandler(false)),
                Arguments.of(123, 123, Integer.class, new IntTypeHandler(true)),
                Arguments.of(null, null, Integer.class, new IntTypeHandler(true)),
                Arguments.of(-999, -999, int.class, new IntTypeHandler(false)),
                Arguments.of(9L, 9L, Long.class, new LongTypeHandler(true)),
                Arguments.of(null, null, Long.class, new LongTypeHandler(true)),
                Arguments.of(-2L, -2L, long.class, new LongTypeHandler(false)),
                Arguments.of(0.33f, 0.33f, Float.class, new FloatTypeHandler(true)),
                Arguments.of(null, null, Float.class, new FloatTypeHandler(true)),
                Arguments.of(-.1111f, -.1111f, float.class, new FloatTypeHandler(false)),
                Arguments.of(0.992, 0.992, Double.class, new DoubleTypeHandler(true)),
                Arguments.of(null, null, Double.class, new DoubleTypeHandler(true)),
                Arguments.of(-.1111, -.1111, double.class, new DoubleTypeHandler(false)),
                Arguments.of(BigDecimal.valueOf(12345), BigDecimal.valueOf(12345), BigDecimal.class, new BigDecimalTypeHandler()),
                Arguments.of(null, null, BigDecimal.class, new BigDecimalTypeHandler()),

                Arguments.of(true, "TRuE", Boolean.class, new StringBooleanTypeHandler(true)),
                Arguments.of(null, null, Boolean.class, new StringBooleanTypeHandler(true)),
                Arguments.of(false, "false", boolean.class, new StringBooleanTypeHandler(false))
        );
    }

    @ParameterizedTest
    @MethodSource("valueReaderDataProvider")
    void testValueReader(Object expected, Object given, Class<?> clazz, AbstractSimpleColumnTypeHandler typeHandler) throws Exception {
        ResultSet rs = new VirtualResultSet(
                new String[]{"foo"},
                new Object[]{given}
        );

        Object v = typeHandler.getValueReader("foo", clazz).getValue(rs);
        Assertions.assertEquals(expected, v);
    }
}
