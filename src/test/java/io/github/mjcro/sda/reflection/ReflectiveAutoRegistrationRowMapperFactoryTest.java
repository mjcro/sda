package io.github.mjcro.sda.reflection;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.lang.reflect.Field;
import java.util.List;

public class ReflectiveAutoRegistrationRowMapperFactoryTest {
    @Test
    public void testRecursiveFields() {
        List<Field> fields = ReflectiveAutoRegistrationRowMapperFactory.recursiveFields(FieldTargetB.class);
        Assert.assertEquals(fields.size(), 2);
        Assert.assertEquals(fields.get(0).getName(), "b");
        Assert.assertEquals(fields.get(1).getName(), "a");
    }

    static class FieldTargetA {
        private static int StaticA;
        private final int finalA = 2;
        private int a;
    }

    static class FieldTargetB extends FieldTargetA {
        public static String StaticB;
        private String b;
    }
}