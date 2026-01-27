package io.github.mjcro.sda.reflection;

import io.github.mjcro.sda.Creator;
import io.github.mjcro.sda.TypeHandler;
import io.github.mjcro.sda.VirtualColumn;
import io.github.mjcro.sda.VirtualResultSet;
import io.github.mjcro.sda.reflection.CreatorAnnotationTypeHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CreatorAnnotationTypeHandlerTest {
    @Test
    void testStaticFactoryMethod() throws Exception {
        VirtualResultSet rs = new VirtualResultSet(
                new String[]{"name"},
                new Object[]{"world"}
        );

        TypeHandler h = new CreatorAnnotationTypeHandler();
        Object v = h.getValueReader(new VirtualColumn<>("name", GreeterStatic.class)).getValue(rs);
        Assertions.assertInstanceOf(GreeterStatic.class, v);
        Assertions.assertEquals("Hello, world", ((GreeterStatic) v).value);
    }

    @Test
    void testConstructor() throws Exception {
        VirtualResultSet rs = new VirtualResultSet(
                new String[]{"name"},
                new Object[]{"foo"}
        );

        TypeHandler h = new CreatorAnnotationTypeHandler();
        Object v = h.getValueReader(new VirtualColumn<>("name", GreeterConstructor.class)).getValue(rs);
        Assertions.assertInstanceOf(GreeterConstructor.class, v);
        Assertions.assertEquals("Hello, foo", ((GreeterConstructor) v).value);
    }

    private static class GreeterStatic {
        private final String value;

        @Creator
        private static GreeterStatic dbCreator(String value) {
            return new GreeterStatic("Hello, " + value);
        }

        private GreeterStatic(String value) {
            this.value = value;
        }
    }

    private static class GreeterConstructor {
        private final String value;

        @Creator
        private GreeterConstructor(String value) {
            this.value = "Hello, " + value;
        }
    }
}