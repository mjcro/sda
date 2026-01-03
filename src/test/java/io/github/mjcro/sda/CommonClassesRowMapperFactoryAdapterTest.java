package io.github.mjcro.sda;

import org.jspecify.annotations.NonNull;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CommonClassesRowMapperFactoryAdapterTest {
    @Test
    public void testLongs() throws SQLException {
        ResultSet rs = new VirtualResultSet(new String[]{"id"}, new Object[]{63L});

        Long value = new CommonClassesRowMapperFactoryAdapter(new RowMapperFactory() {
            @Override
            public @NonNull <T> RowMapper<T> get(@NonNull Class<T> clazz) {
                return null;
            }
        }).get(long.class).mapRow(rs);

        Assert.assertEquals(value, 63L);
    }

    @Test
    public void testInts() throws SQLException {
        ResultSet rs = new VirtualResultSet(new String[]{"id"}, new Object[]{122});

        Integer value = new CommonClassesRowMapperFactoryAdapter(new RowMapperFactory() {
            @Override
            public @NonNull <T> RowMapper<T> get(@NonNull Class<T> clazz) {
                return null;
            }
        }).get(int.class).mapRow(rs);

        Assert.assertEquals(value, 122);
    }

    @Test
    public void testStrings() throws SQLException {
        ResultSet rs = new VirtualResultSet(new String[]{"id"}, new Object[]{"Some foo"});

        String value = new CommonClassesRowMapperFactoryAdapter(new RowMapperFactory() {
            @Override
            public @NonNull <T> RowMapper<T> get(@NonNull Class<T> clazz) {
                return null;
            }
        }).get(String.class).mapRow(rs);

        Assert.assertEquals(value, "Some foo");
    }
}