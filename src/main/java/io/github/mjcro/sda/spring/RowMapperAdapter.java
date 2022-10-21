package io.github.mjcro.sda.spring;

import io.github.mjcro.sda.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

/**
 * Adapter over Spring's RowMapper to make it compatible.
 *
 * @param <T> Producing type.
 */
public class RowMapperAdapter<T> implements RowMapper<T> {
    private final org.springframework.jdbc.core.RowMapper<T> spring;

    /**
     * Constructs new RowMapper using Spring's one.
     *
     * @param spring Spring RowMapper.
     */
    public RowMapperAdapter(org.springframework.jdbc.core.RowMapper<T> spring) {
        this.spring = Objects.requireNonNull(spring, "inner");
    }

    @Override
    public T mapRow(ResultSet rs) throws SQLException {
        return spring.mapRow(rs, 0);
    }
}
