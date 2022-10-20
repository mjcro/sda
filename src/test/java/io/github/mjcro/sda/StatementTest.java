package io.github.mjcro.sda;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.LinkedHashMap;
import java.util.List;

public class StatementTest {
    @Test
    public void testOf() {
        Statement statement = Statement.of("Foo", null);
        Assert.assertTrue(statement instanceof StatementQueryOnly);
        Assert.assertEquals(statement.getSql(), "Foo");
        Assert.assertNotNull(statement.getPlaceholders());
        Assert.assertEquals(statement.getPlaceholders().length, 0);

        statement = Statement.of("Bar", new Object[]{});
        Assert.assertTrue(statement instanceof StatementQueryOnly);
        Assert.assertEquals(statement.getSql(), "Bar");
        Assert.assertNotNull(statement.getPlaceholders());
        Assert.assertEquals(statement.getPlaceholders().length, 0);

        statement = Statement.of("Baz", new Object[]{2L, "xxx"});
        Assert.assertTrue(statement instanceof StatementSimple);
        Assert.assertEquals(statement.getSql(), "Baz");
        Assert.assertNotNull(statement.getPlaceholders());
        Assert.assertEquals(statement.getPlaceholders().length, 2);
        Assert.assertEquals(statement.getPlaceholders()[0], 2L);
        Assert.assertEquals(statement.getPlaceholders()[1], "xxx");
    }

    @Test(dependsOnMethods = "testOf")
    public void testMatchColumnMySQL() {
        Statement statement = Statement.matchColumn("content", "id", List.of(3, 4, 5))
                .createStatement(Dialect.MySQL);

        Assert.assertEquals(statement.getSql(), "SELECT * FROM `content` WHERE `id` IN (?,?,?)");
        Assert.assertEquals(statement.getPlaceholders()[0], 3);
        Assert.assertEquals(statement.getPlaceholders()[1], 4);
        Assert.assertEquals(statement.getPlaceholders()[2], 5);

        statement = Statement.matchColumn("content", "enabled", List.of(false))
                .createStatement(Dialect.MySQL);

        Assert.assertEquals(statement.getSql(), "SELECT * FROM `content` WHERE `enabled` = ?");
        Assert.assertEquals(statement.getPlaceholders()[0], false);
    }

    @Test(dependsOnMethods = "testOf")
    public void testMatchColumn2MySQL() {
        Statement statement = Statement.matchColumn("content", "id", List.of(3, 4, 5), "enabled", List.of(true))
                .createStatement(Dialect.MySQL);

        Assert.assertEquals(statement.getSql(), "SELECT * FROM `content` WHERE `id` IN (?,?,?) AND `enabled` = ?");
        Assert.assertEquals(statement.getPlaceholders()[0], 3);
        Assert.assertEquals(statement.getPlaceholders()[1], 4);
        Assert.assertEquals(statement.getPlaceholders()[2], 5);
        Assert.assertEquals(statement.getPlaceholders()[3], true);
    }

    @Test(dependsOnMethods = "testOf")
    public void testInsert() {
        LinkedHashMap<String, Object> values = new LinkedHashMap<>();
        values.put("id", 33L);
        values.put("name", "Fooer");

        Statement statement = Statement.insert("fooTable", values)
                .createStatement(Dialect.MySQL);

        Assert.assertEquals(statement.getSql(), "INSERT INTO `fooTable` (`id`,`name`) VALUES (?,?)");
        Assert.assertEquals(statement.getPlaceholders()[0], 33L);
        Assert.assertEquals(statement.getPlaceholders()[1], "Fooer");
    }
}