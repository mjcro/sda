package io.github.mjcro.sda.reflection;

import io.github.mjcro.sda.Column;
import io.github.mjcro.sda.Creator;
import io.github.mjcro.sda.Mapper;
import io.github.mjcro.sda.RowMapper;
import io.github.mjcro.sda.TieredTypeHandlerList;
import io.github.mjcro.sda.VirtualResultSet;
import io.github.mjcro.sda.reflection.BigDecimalTypeHandler;
import io.github.mjcro.sda.reflection.ByteArrayTypeHandler;
import io.github.mjcro.sda.reflection.ByteTypeHandler;
import io.github.mjcro.sda.reflection.CreatorAnnotationTypeHandler;
import io.github.mjcro.sda.reflection.DoubleTypeHandler;
import io.github.mjcro.sda.reflection.DurationNanosTypeHandler;
import io.github.mjcro.sda.reflection.EntityFieldsTypeHandler;
import io.github.mjcro.sda.reflection.EnumIntegerIdTypeHandler;
import io.github.mjcro.sda.reflection.EnumLongIdTypeHandler;
import io.github.mjcro.sda.reflection.EnumNameTypeHandler;
import io.github.mjcro.sda.reflection.FloatTypeHandler;
import io.github.mjcro.sda.reflection.IntTypeHandler;
import io.github.mjcro.sda.reflection.LongTypeHandler;
import io.github.mjcro.sda.reflection.MapperAnnotationTypeHandler;
import io.github.mjcro.sda.reflection.ShortTypeHandler;
import io.github.mjcro.sda.reflection.StringBooleanTypeHandler;
import io.github.mjcro.sda.reflection.StringTypeHandler;
import io.github.mjcro.sda.reflection.TypeHandlerRowMapperFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.time.Duration;
import java.util.AbstractMap;

class EntityFieldsTypeHandlerTest {
    @Test
    void testEntity() throws Exception {
        TieredTypeHandlerList handlerList = new TieredTypeHandlerList();
        handlerList
                .addLast(
                        TieredTypeHandlerList.Tier.ENTITY_CONSTRUCTORS,
                        new EntityFieldsTypeHandler(handlerList)
                )
                .addLast(
                        TieredTypeHandlerList.Tier.ANNOTATION_PROCESSORS,
                        new MapperAnnotationTypeHandler(),
                        new CreatorAnnotationTypeHandler()
                )
                .addLast(
                        TieredTypeHandlerList.Tier.TYPES,
                        new StringTypeHandler(),
                        new ByteTypeHandler(true),
                        new ByteTypeHandler(false),
                        new ShortTypeHandler(true),
                        new ShortTypeHandler(false),
                        new IntTypeHandler(true),
                        new IntTypeHandler(false),
                        new LongTypeHandler(true),
                        new LongTypeHandler(false),
                        new FloatTypeHandler(true),
                        new FloatTypeHandler(false),
                        new DoubleTypeHandler(true),
                        new DoubleTypeHandler(false),
                        new ByteArrayTypeHandler(),
                        new BigDecimalTypeHandler(),
                        new EnumIntegerIdTypeHandler(),
                        new EnumLongIdTypeHandler(),
                        new EnumNameTypeHandler()
                );
        TypeHandlerRowMapperFactory f = new TypeHandlerRowMapperFactory(handlerList);


        ResultSet rs = VirtualResultSet.ofEntries(
                new AbstractMap.SimpleEntry<>("name", "The Name"),
                new AbstractMap.SimpleEntry<>("id", 39L),
                new AbstractMap.SimpleEntry<>("enabled", "true"),
                new AbstractMap.SimpleEntry<>("ttl", 3600_000_000_000L),
                new AbstractMap.SimpleEntry<>("b1", (byte) 11),
                new AbstractMap.SimpleEntry<>("s1", (short) 2222),
                new AbstractMap.SimpleEntry<>("i1", 8126),
                new AbstractMap.SimpleEntry<>("bd1", new BigDecimal("12.3456")),
                new AbstractMap.SimpleEntry<>("ba1", "11".getBytes(StandardCharsets.UTF_8)),
                new AbstractMap.SimpleEntry<>("ip", "8.8.8.8"),
                new AbstractMap.SimpleEntry<>("f1", 0.999f),
                new AbstractMap.SimpleEntry<>("d1", -123.1),
                new AbstractMap.SimpleEntry<>("u1", "admin"),
                new AbstractMap.SimpleEntry<>("u2", "USER"),
                new AbstractMap.SimpleEntry<>("u3", "guest"),
                new AbstractMap.SimpleEntry<>("il1", 2),
                new AbstractMap.SimpleEntry<>("ll1", 1L)
        );

        RowMapper<Entity> mapper = f.get(Entity.class);
        Entity row = mapper.mapRow(rs);

        Assertions.assertNotNull(row);
        Assertions.assertEquals(39L, row.id);
        Assertions.assertNull(row.parent);
        Assertions.assertNull(row.privileged);
        Assertions.assertTrue(row.enabled);
        Assertions.assertEquals(Duration.ofHours(1), row.ttl);
        Assertions.assertEquals((byte) 11, row.b1);
        Assertions.assertNull(row.b2);
        Assertions.assertEquals((short) 2222, row.s1);
        Assertions.assertNull(row.s2);
        Assertions.assertEquals(8126, row.i1);
        Assertions.assertNull(row.i2);
        Assertions.assertEquals(0.999f, row.f1);
        Assertions.assertNull(row.f2);
        Assertions.assertEquals(-123.1, row.d1);
        Assertions.assertNull(row.d2);
        Assertions.assertEquals(new BigDecimal("12.3456"), row.bd1);
        Assertions.assertNull(row.bd2);
        Assertions.assertArrayEquals("11".getBytes(StandardCharsets.UTF_8), row.ba1);
        Assertions.assertNull(row.ba2);
        Assertions.assertEquals(InetAddress.getByName("8.8.8.8"), row.ip.ip);
        Assertions.assertNull(row.proxyIp);
        Assertions.assertEquals(UserType.Admin, row.u1);
        Assertions.assertEquals(UserType.USER, row.u2);
        Assertions.assertEquals(UserType.guest, row.u3);
        Assertions.assertNull(row.u4);
        Assertions.assertEquals(IntLevel.SECOND, row.il1);
        Assertions.assertNull(row.il2);
        Assertions.assertEquals(LongLevel.FIRST, row.ll1);
        Assertions.assertNull(row.ll2);
    }

    private static class Entity {
        @Column("name")
        private String name;
        @Column("displayName")
        private String displayName;
        @Column("id")
        private long id;
        @Column("parentId")
        private Long parent;
        @Column("privileged")
        @Mapper(StringBooleanTypeHandler.class)
        private Boolean privileged;
        @Column("enabled")
        @Mapper(StringBooleanTypeHandler.class)
        private boolean enabled;
        @Column("ttl")
        @Mapper(DurationNanosTypeHandler.class)
        private Duration ttl;

        @Column("b1")
        private byte b1;
        @Column("b2")
        private Byte b2;
        @Column("s1")
        private short s1;
        @Column("s2")
        private Short s2;
        @Column("i1")
        private int i1;
        @Column("i2")
        private Integer i2;
        @Column("f1")
        private float f1;
        @Column("f2")
        private Float f2;
        @Column("d1")
        private double d1;
        @Column("d2")
        private Double d2;
        @Column("bd1")
        private BigDecimal bd1;
        @Column("bd2")
        private BigDecimal bd2;
        @Column("ba1")
        private byte[] ba1;
        @Column("ba2")
        private byte[] ba2;

        @Column("ip")
        private WrappedIp ip;
        @Column("proxyIp")
        private WrappedIp proxyIp;

        @Column("u1")
        private UserType u1;
        @Column("u2")
        private UserType u2;
        @Column("u3")
        private UserType u3;
        @Column("u4")
        private UserType u4;

        @Column("il1")
        private IntLevel il1;
        @Column("il2")
        private IntLevel il2;
        @Column("ll1")
        private LongLevel ll1;
        @Column("ll2")
        private LongLevel ll2;
    }

    private static class WrappedIp {
        private final InetAddress ip;

        @Creator
        private WrappedIp(String value) throws UnknownHostException {
            this.ip = InetAddress.getByName(value);
        }
    }

    private enum UserType {
        Admin, USER, guest;
    }

    private enum IntLevel implements io.github.mjcro.interfaces.ints.WithId {
        NONE(0),
        FIRST(1),
        SECOND(2);

        private final int id;

        IntLevel(int id) {
            this.id = id;
        }

        @Override
        public int getId() {
            return id;
        }
    }

    private enum LongLevel implements io.github.mjcro.interfaces.longs.WithId {
        NONE(0),
        FIRST(1),
        SECOND(2);

        private final long id;

        LongLevel(long id) {
            this.id = id;
        }

        @Override
        public long getId() {
            return id;
        }
    }
}