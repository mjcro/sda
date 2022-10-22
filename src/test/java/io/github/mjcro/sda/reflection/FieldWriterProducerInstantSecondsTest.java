package io.github.mjcro.sda.reflection;

import io.github.mjcro.sda.FieldWriter;
import io.github.mjcro.sda.VirtualResultSet;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;

public class FieldWriterProducerInstantSecondsTest {
    @SuppressWarnings("unchecked")
    @Test
    public void testApply() throws NoSuchFieldException, SQLException {
        FieldWriter<Target> fieldWriter = (FieldWriter<Target>) new FieldWriterProducerInstantSeconds().apply(
                Target.class.getDeclaredField("instant"),
                "instant"
        ).get();

        Target target = new Target();
        ResultSet rs = new VirtualResultSet(
                new String[]{"instant"},
                new Object[]{123456789L}
        );

        fieldWriter.write(rs, target);
        Assert.assertNotNull(target.instant);
        Assert.assertEquals(target.instant, Instant.ofEpochSecond(123456789L));
    }

    private static class Target {
        public Instant instant;
    }
}