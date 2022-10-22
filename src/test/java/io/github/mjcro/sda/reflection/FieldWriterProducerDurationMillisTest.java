package io.github.mjcro.sda.reflection;

import io.github.mjcro.sda.FieldWriter;
import io.github.mjcro.sda.VirtualResultSet;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;

public class FieldWriterProducerDurationMillisTest {
    @SuppressWarnings("unchecked")
    @Test
    public void testApply() throws NoSuchFieldException, SQLException {
        FieldWriter<Target> fieldWriter = (FieldWriter<Target>) new FieldWriterProducerDurationMillis().apply(
                Target.class.getDeclaredField("duration"),
                "duration"
        ).get();

        Target target = new Target();
        ResultSet rs = new VirtualResultSet(
                new String[]{"duration"},
                new Object[]{12345L}
        );

        fieldWriter.write(rs, target);
        Assert.assertNotNull(target.duration);
        Assert.assertEquals(target.duration, Duration.ofMillis(12345));
    }

    private static class Target {
        public Duration duration;
    }
}