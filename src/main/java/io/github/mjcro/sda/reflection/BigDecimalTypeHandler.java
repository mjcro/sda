package io.github.mjcro.sda.reflection;

import org.jspecify.annotations.NonNull;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Reads {@link BigDecimal} columns via {@link java.sql.ResultSet#getBigDecimal(String)}.
 */
public class BigDecimalTypeHandler extends AbstractSimpleColumnTypeHandler {
    @Override
    protected boolean supports(@NonNull Class<?> clazz) {
        return Objects.equals(BigDecimal.class, clazz);
    }

    @Override
    protected ValueReader getValueReader(@NonNull String column) {
        return rs -> rs.getBigDecimal(column);
    }
}