package io.github.mjcro.sda.reflection;

import io.github.mjcro.sda.Creator;
import io.github.mjcro.sda.FieldWriter;
import io.github.mjcro.sda.VirtualResultSet;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;

public class FieldWriterProducerByCreatorAnnotationTest {

    @Test
    public void testApply() throws SQLException, NoSuchFieldException {
        FieldWriter<Target> fieldWriter = (FieldWriter<Target>) new FieldWriterProducerByCreatorAnnotation().apply(
                Target.class.getDeclaredField("custom"),
                "foo"
        ).get();

        Target target = new Target();
        ResultSet rs = new VirtualResultSet(
                new String[]{"foo"},
                new Object[]{35L}
        );

        fieldWriter.write(rs, target);
        Assert.assertNotNull(target.custom);
        Assert.assertEquals(target.custom.value, "+35+");
    }

    private static class Target {
        public Custom custom;
    }

    private static class Custom {
        final String value;

        private Custom(final String value) {
            this.value = value;
        }

        @Creator
        private static Custom create(long x) {
            return new Custom("+" + x + "+");
        }
    }
}