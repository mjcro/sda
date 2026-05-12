package io.github.mjcro.sda;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Maps a single row of a {@link ResultSet} into an instance of {@code T}.
 *
 * @param <T> Type of the produced domain object.
 */
@FunctionalInterface
public interface RowMapper<T> {
    /**
     * Reads a single row from the result set and converts it to a domain object.
     * The cursor must already be positioned on the target row; implementation MUST NOT
     * call {@link ResultSet#next()}.
     *
     * @param rs Source result set, not null.
     * @return Mapped instance, may be null.
     * @throws SQLException On any result set access error.
     */
    @Nullable T mapRow(@NonNull ResultSet rs) throws SQLException;
}
