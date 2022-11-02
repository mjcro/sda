package io.github.mjcro.sda.reflection;

import io.github.mjcro.sda.FieldWriter;
import io.github.mjcro.sda.VirtualResultSet;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FieldWriterProducerUppercaseEnumsTest {
    @SuppressWarnings("unchecked")
    @Test
    public void testApply() throws NoSuchFieldException, SQLException {
        FieldWriter<Target> fieldWriter = (FieldWriter<Target>) new FieldWriterProducerUppercaseEnums().apply(
                Target.class.getDeclaredField("yesNo"),
                "enabled"
        ).get();

        Target target = new Target();
        ResultSet rs = new VirtualResultSet(
                new String[]{"enabled"},
                new Object[]{"yEs"}
        );

        fieldWriter.write(rs, target);
        Assert.assertNotNull(target.yesNo);
        Assert.assertEquals(target.yesNo, YesNo.YES);

        rs = new VirtualResultSet(
                new String[]{"enabled"},
                new Object[]{"nO"}
        );

        fieldWriter.write(rs, target);
        Assert.assertNotNull(target.yesNo);
        Assert.assertEquals(target.yesNo, YesNo.NO);

    }

    private static class Target {
        public YesNo yesNo;
    }

    private enum YesNo {
        YES, NO;
    }
}