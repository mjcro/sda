package io.github.mjcro.sda;

import io.github.mjcro.interfaces.database.Statement;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.List;

public class StatementsTest {
    @Test
    public void testOf() {
        Statement statement = Statements.of("Foo", null);
        Assertions.assertInstanceOf(StatementQueryOnly.class, statement);
        Assertions.assertEquals("Foo", statement.getSql());
        Assertions.assertNotNull(statement.getParameters());
        Assertions.assertEquals(0, statement.getParameters().length);

        statement = Statements.of("Bar", new Object[]{});
        Assertions.assertInstanceOf(StatementQueryOnly.class, statement);
        Assertions.assertEquals("Bar", statement.getSql());
        Assertions.assertNotNull(statement.getParameters());
        Assertions.assertEquals(0, statement.getParameters().length);

        statement = Statements.of("Baz", new Object[]{2L, "xxx"});
        Assertions.assertInstanceOf(StatementSimple.class, statement);
        Assertions.assertEquals("Baz", statement.getSql());
        Assertions.assertNotNull(statement.getParameters());
        Assertions.assertEquals(2, statement.getParameters().length);
        Assertions.assertEquals(2L, statement.getParameters()[0]);
        Assertions.assertEquals("xxx", statement.getParameters()[1]);
    }

    @Test
    public void testMatchColumnMySQL() {
        Statement statement = Statements.matchColumn("content", "id", List.of(3, 4, 5))
                .createStatement(Dialect.MySQL);

        Assertions.assertEquals("SELECT * FROM `content` WHERE `id` IN (?,?,?)", statement.getSql());
        Assertions.assertEquals(3, statement.getParameters()[0]);
        Assertions.assertEquals(4, statement.getParameters()[1]);
        Assertions.assertEquals(5, statement.getParameters()[2]);

        statement = Statements.matchColumn("content", "enabled", List.of(false))
                .createStatement(Dialect.MySQL);

        Assertions.assertEquals("SELECT * FROM `content` WHERE `enabled`=?", statement.getSql());
        Assertions.assertEquals(false, statement.getParameters()[0]);
    }

    @Test
    public void testMatchColumn2MySQL() {
        Statement statement = Statements.matchColumn("content", "id", List.of(3, 4, 5), "enabled", List.of(true))
                .createStatement(Dialect.MySQL);

        Assertions.assertEquals("SELECT * FROM `content` WHERE `id` IN (?,?,?) AND `enabled`=?", statement.getSql());
        Assertions.assertEquals(3, statement.getParameters()[0]);
        Assertions.assertEquals(4, statement.getParameters()[1]);
        Assertions.assertEquals(5, statement.getParameters()[2]);
        Assertions.assertEquals(true, statement.getParameters()[3]);
    }

    @Test
    public void testInsert() {
        LinkedHashMap<String, Object> values = new LinkedHashMap<>();
        values.put("id", 33L);
        values.put("name", "Fooer");

        Statement statement = Statements.insert("fooTable", values)
                .createStatement(Dialect.MySQL);

        Assertions.assertEquals("INSERT INTO `fooTable` (`id`,`name`) VALUES (?,?)", statement.getSql());
        Assertions.assertEquals(33L, statement.getParameters()[0]);
        Assertions.assertEquals("Fooer", statement.getParameters()[1]);
    }

    @Test
    public void testEquality() {
        Statement a = new StatementQueryOnly("SELECT * FROM `user`");
        Statement b = new StatementSimple("SELECT * FROM `user`", new Object[0]);
        Statement c = new StatementSimple("SELECT * FROM `user`", new Object[]{true});

        Assertions.assertEquals(a, b);
        Assertions.assertEquals(b, a);
        Assertions.assertEquals(a.hashCode(), b.hashCode());

        Assertions.assertNotEquals(a, c);
        Assertions.assertNotEquals(b, c);
        Assertions.assertNotEquals(c, a);
        Assertions.assertNotEquals(c, b);
    }
}