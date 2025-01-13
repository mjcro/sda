package io.github.mjcro.sda;

import io.github.mjcro.interfaces.database.Statement;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.LinkedHashMap;
import java.util.List;

public class StatementsTest {
    @Test
    public void testOf() {
        Statement statement = Statements.of("Foo", null);
        Assert.assertTrue(statement instanceof StatementQueryOnly);
        Assert.assertEquals(statement.getSql(), "Foo");
        Assert.assertNotNull(statement.getParameters());
        Assert.assertEquals(statement.getParameters().length, 0);

        statement = Statements.of("Bar", new Object[]{});
        Assert.assertTrue(statement instanceof StatementQueryOnly);
        Assert.assertEquals(statement.getSql(), "Bar");
        Assert.assertNotNull(statement.getParameters());
        Assert.assertEquals(statement.getParameters().length, 0);

        statement = Statements.of("Baz", new Object[]{2L, "xxx"});
        Assert.assertTrue(statement instanceof StatementSimple);
        Assert.assertEquals(statement.getSql(), "Baz");
        Assert.assertNotNull(statement.getParameters());
        Assert.assertEquals(statement.getParameters().length, 2);
        Assert.assertEquals(statement.getParameters()[0], 2L);
        Assert.assertEquals(statement.getParameters()[1], "xxx");
    }

    @Test(dependsOnMethods = "testOf")
    public void testMatchColumnMySQL() {
        Statement statement = Statements.matchColumn("content", "id", List.of(3, 4, 5))
                .createStatement(Dialect.MySQL);

        Assert.assertEquals(statement.getSql(), "SELECT * FROM `content` WHERE `id` IN (?,?,?)");
        Assert.assertEquals(statement.getParameters()[0], 3);
        Assert.assertEquals(statement.getParameters()[1], 4);
        Assert.assertEquals(statement.getParameters()[2], 5);

        statement = Statements.matchColumn("content", "enabled", List.of(false))
                .createStatement(Dialect.MySQL);

        Assert.assertEquals(statement.getSql(), "SELECT * FROM `content` WHERE `enabled` = ?");
        Assert.assertEquals(statement.getParameters()[0], false);
    }

    @Test(dependsOnMethods = "testOf")
    public void testMatchColumn2MySQL() {
        Statement statement = Statements.matchColumn("content", "id", List.of(3, 4, 5), "enabled", List.of(true))
                .createStatement(Dialect.MySQL);

        Assert.assertEquals(statement.getSql(), "SELECT * FROM `content` WHERE `id` IN (?,?,?) AND `enabled` = ?");
        Assert.assertEquals(statement.getParameters()[0], 3);
        Assert.assertEquals(statement.getParameters()[1], 4);
        Assert.assertEquals(statement.getParameters()[2], 5);
        Assert.assertEquals(statement.getParameters()[3], true);
    }

    @Test(dependsOnMethods = "testOf")
    public void testInsert() {
        LinkedHashMap<String, Object> values = new LinkedHashMap<>();
        values.put("id", 33L);
        values.put("name", "Fooer");

        Statement statement = Statements.insert("fooTable", values)
                .createStatement(Dialect.MySQL);

        Assert.assertEquals(statement.getSql(), "INSERT INTO `fooTable` (`id`,`name`) VALUES (?,?)");
        Assert.assertEquals(statement.getParameters()[0], 33L);
        Assert.assertEquals(statement.getParameters()[1], "Fooer");
    }

    @Test
    public void testEquality() {
        Statement a = new StatementQueryOnly("SELECT * FROM `user`");
        Statement b = new StatementSimple("SELECT * FROM `user`", new Object[0]);
        Statement c = new StatementSimple("SELECT * FROM `user`", new Object[]{true});

        Assert.assertEquals(a, b);
        Assert.assertEquals(b, a);
        Assert.assertEquals(a.hashCode(), b.hashCode());

        Assert.assertNotEquals(a, c);
        Assert.assertNotEquals(b, c);
        Assert.assertNotEquals(c, a);
        Assert.assertNotEquals(c, b);
    }
}