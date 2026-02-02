package io.github.mjcro.sda;

import io.github.mjcro.interfaces.database.Statement;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.TreeMap;

public class MySqlUtilTest {
    @Test
    void matchColumn() {
        Statement stmt = MySqlUtil.matchColumn(
                "atable",
                "idd",
                Set.of(2L)
        );
        Assertions.assertEquals("SELECT * FROM `atable` WHERE `idd`=?", stmt.getSql());
        Assertions.assertEquals(2L, stmt.getParameters()[0]);

        stmt = MySqlUtil.matchColumn(
                "xxx",
                "parent_id",
                List.of(9, 7)
        );

        Assertions.assertEquals("SELECT * FROM `xxx` WHERE `parent_id` IN (?,?)", stmt.getSql());
        Assertions.assertEquals(9, stmt.getParameters()[0]);
        Assertions.assertEquals(7, stmt.getParameters()[1]);
    }

    @Test
    void match2Columns() {
        Statement stmt = MySqlUtil.matchColumn(
                "xxx",
                "parent_id",
                List.of(19),
                "enabled",
                List.of("true")
        );

        Assertions.assertEquals(
                "SELECT * FROM `xxx` WHERE `parent_id`=? AND `enabled`=?",
                stmt.getSql()
        );
        Assertions.assertEquals(19, stmt.getParameters()[0]);
        Assertions.assertEquals("true", stmt.getParameters()[1]);

        stmt = MySqlUtil.matchColumn(
                "yyy",
                "category_id",
                List.of(19, 88),
                "state",
                List.of("active", "hidden")
        );
        Assertions.assertEquals(
                "SELECT * FROM `yyy` WHERE `category_id` IN (?,?) AND `state` IN (?,?)",
                stmt.getSql()
        );
        Assertions.assertEquals(19, stmt.getParameters()[0]);
        Assertions.assertEquals(88, stmt.getParameters()[1]);
        Assertions.assertEquals("active", stmt.getParameters()[2]);
        Assertions.assertEquals("hidden", stmt.getParameters()[3]);
    }

    @Test
    void insert() {
        TreeMap<String, Object> values = new TreeMap<>();
        values.put("a", null);
        values.put("b", 12345);
        values.put("c", "bar");

        Statement stmt = MySqlUtil.insert("abc", values);
        Assertions.assertEquals(
                "INSERT INTO `abc` (`a`,`b`,`c`) VALUES (?,?,?)",
                stmt.getSql()
        );

        Object[] p = stmt.getParameters();
        Assertions.assertEquals(3, p.length);
        Assertions.assertNull(p[0]);
        Assertions.assertEquals(12345, p[1]);
        Assertions.assertEquals("bar", p[2]);
    }

    @Test
    void updateByColumn() {
        TreeMap<String, Object> values = new TreeMap<>();
        values.put("a", null);
        values.put("b", 12345);
        values.put("c", "bar");
        Statement stmt = MySqlUtil.updateByColumn("footable", "fooid", 11L, values);

        Assertions.assertEquals(
                "UPDATE `footable` SET `a`=?,`b`=?,`c`=? WHERE `fooid`=?",
                stmt.getSql()
        );
        Object[] p = stmt.getParameters();
        Assertions.assertEquals(4, p.length);
        Assertions.assertNull(p[0]);
        Assertions.assertEquals(12345, p[1]);
        Assertions.assertEquals("bar", p[2]);
        Assertions.assertEquals(11L, p[3]);
    }

    @Test
    void deleteByColumn() {
        Statement stmt = MySqlUtil.deleteByColumn("xxx", "xid", 122L);
        Assertions.assertEquals(
                "DELETE FROM `xxx` WHERE `xid`=?",
                stmt.getSql()
        );
        Object[] p = stmt.getParameters();
        Assertions.assertEquals(1, p.length);
        Assertions.assertEquals(122L, p[0]);
    }
}