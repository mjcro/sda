package io.github.mjcro.sda.reflection;

import io.github.mjcro.references.longs.IdReference;
import io.github.mjcro.sda.FieldWriter;
import io.github.mjcro.sda.VirtualResultSet;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FieldWriterProducerIdEnumTest {
    @SuppressWarnings("unchecked")
    @Test
    public void testApply() throws NoSuchFieldException, SQLException {
        FieldWriter<Target> fieldWriter = (FieldWriter<Target>) new FieldWriterProducerIdEnum().apply(
                Target.class.getDeclaredField("userType"),
                "userType"
        ).get();

        Target target = new Target();
        ResultSet rs = new VirtualResultSet(
                new String[]{"userType"},
                new Object[]{2L}
        );

        fieldWriter.write(rs, target);
        Assert.assertNotNull(target.userType);
        Assert.assertEquals(target.userType, UserType.ADMIN);

        rs = new VirtualResultSet(
                new String[]{"userType"},
                new Object[]{1L}
        );

        fieldWriter.write(rs, target);
        Assert.assertNotNull(target.userType);
        Assert.assertEquals(target.userType, UserType.USER);
    }

    private static class Target {
        public UserType userType;
    }

    private enum UserType implements IdReference {
        ADMIN(2),
        USER(1),
        ANONYMOUS(0);

        UserType(long id) {
            this.id = id;
        }

        private final long id;

        @Override
        public long getId() {
            return id;
        }
    }
}