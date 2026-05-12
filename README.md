# SDA — Simple Database Adapter

Tiny JDBC wrapper. Maps `ResultSet` rows to Java objects without an ORM.

## Coordinates

```xml
<dependency>
    <groupId>io.github.mjcro</groupId>
    <artifactId>sda</artifactId>
    <version>2.0.4</version>
</dependency>
```

Requires Java 9+. MIT licensed.

## Core types

- `SqlInvoker` — read-only: `list`, `one`, `map`, `grouped`.
- `SqlModifier` — adds `modify` for INSERT/UPDATE/DELETE.
- `Dialect` — `Dialect.MySQL`, `Dialect.H2`. Converts SQL errors to typed `DatabaseException` subclasses (`DeadlockException`, `ConstraintViolationException`, `TimeoutException`, `SchemaNotFoundException`).
- `RowMapperFactory` — produces `RowMapper<T>` per target class.
- `Statement` / `StatementPrototype` — SQL + parameters; prototype defers SQL generation to a dialect.
- `SqlTracer` — observability hook called after every query.

## Mapping

Reflection-based via `TieredTypeHandlerList` + handlers in `io.github.mjcro.sda.reflection`:

- Scalars: `int`/`long`/`short`/`byte`/`float`/`double` (+ boxed), `String`, `BigDecimal`, `byte[]`, `LocalDate`, `Instant` (epoch seconds), `Duration` (nanos/millis/seconds).
- Enums: by `name()`, by `WithId` (`int`/`long`).
- Strong types: `StrongLong`, `StrongInt`, `StrongType<String>`.
- Entities: single all-args constructor (`EntityConstructorTypeHandler`) or no-arg + fields (`EntityFieldsTypeHandler`).
- Per-field override: `@Column("col_name")`, `@Mapper(MyHandler.class)`, `@Creator` on constructor/static factory.

## Usage

```java
RowMapperFactory factory = new TypeHandlerRowMapperFactory(handlerList);
SqlModifier db = new BasicSqlModifier(Dialect.MySQL, connectionProvider, factory, tracer);

List<User> users = db.list(User.class, "SELECT * FROM users WHERE active=?", new Object[]{true});
Optional<User> u = db.one(User.class, Statements.matchColumn("users", "id", List.of(42L)));
Optional<Long> id = db.modify(Statements.insert("users", Map.of("name", "Alice")));
```

## Spring

`@Import` one of:

- `SimpleDatabaseAccessConfiguration` — wires `SqlInvoker`/`SqlModifier`/`RowMapperFactory`; expects beans `DataSource` and `Supplier<Dialect>`.
- `MySqlDatabaseAccessConfiguration` — adds a MySQL `DataSource` from `mysql.*` properties.

## Prefab repositories

- `AbstractSqlRepository` — base with time provider and SQL helpers.
- `AbstractEntitySqlRepository<E>` — fetch helpers bound to one entity class.
- `AbstractStoredEntityLongIdSqlRepository` — long-id repository for `Stored.Convertible` entities.
- `SharedLockRepository` — experimental DB-table-backed distributed lock.
