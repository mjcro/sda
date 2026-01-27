package io.github.mjcro.sda.reflection;

import io.github.mjcro.sda.Column;
import io.github.mjcro.sda.Mapper;
import io.github.mjcro.sda.RowMapper;
import io.github.mjcro.sda.TieredTypeHandlerList;
import io.github.mjcro.sda.VirtualResultSet;
import io.github.mjcro.sda.reflection.EntityConstructorTypeHandler;
import io.github.mjcro.sda.reflection.InstantSecondsTypeHandler;
import io.github.mjcro.sda.reflection.IntTypeHandler;
import io.github.mjcro.sda.reflection.MapperAnnotationTypeHandler;
import io.github.mjcro.sda.reflection.StringTypeHandler;
import io.github.mjcro.sda.reflection.TypeHandlerRowMapperFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;

public class EntityConstructorTypeHandlerTest {
    @Test
    void testEntity() throws SQLException {
        TieredTypeHandlerList handlerList = new TieredTypeHandlerList();
        handlerList
                .addFirst(
                        TieredTypeHandlerList.Tier.ENTITY_CONSTRUCTORS,
                        new EntityConstructorTypeHandler(handlerList)
                )
                .addFirst(
                        TieredTypeHandlerList.Tier.ANNOTATION_PROCESSORS,
                        new MapperAnnotationTypeHandler()
                ).addFirst(
                        TieredTypeHandlerList.Tier.TYPES,
                        new StringTypeHandler(),
                        new IntTypeHandler(false)
                );
        TypeHandlerRowMapperFactory f = new TypeHandlerRowMapperFactory(handlerList);

        ResultSet rs = new VirtualResultSet(
                new String[]{
                        "id",
                        "name",
                        "id",
                        "parentId",
                        "privileged",
                        "enabled",
                },
                new Object[]{
                        331,
                        "FOO"
                }
        );

        RowMapper<Entity> mapper = f.get(Entity.class);
        Entity row = mapper.mapRow(rs);

        Assertions.assertNotNull(row);
        Assertions.assertEquals(331, row.id);
        Assertions.assertEquals("FOO", row.name);
    }

    private static class Entity {
        private final int id;
        private final Instant createdAt;
        private final String name;

        private Entity(
                @Column("id") int id,
                @Column("name") String name,
                @Column("created_at") @Mapper(InstantSecondsTypeHandler.class) Instant createdAt
        ) {
            this.id = id;
            this.createdAt = createdAt;
            this.name = name;
        }
    }
}