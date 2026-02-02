package io.github.mjcro.sda;

import io.github.mjcro.sda.exceptions.ConstraintViolationException;
import io.github.mjcro.sda.exceptions.DeadlockException;
import io.github.mjcro.sda.exceptions.SchemaNotFoundException;
import io.github.mjcro.sda.exceptions.TimeoutException;
import org.jspecify.annotations.NonNull;

import java.sql.SQLException;

class DialectMySql implements Dialect {
    @Override
    public @NonNull String getName() {
        return "MySQL";
    }

    @Override
    public @NonNull DatabaseException convertException(@NonNull SQLException source) {
        switch (source.getErrorCode()) {
            case 1213: // MysqlErrorNumbers.ER_LOCK_DEADLOCK
            case 1205: // MysqlErrorNumbers.ER_LOCK_WAIT_TIMEOUT
                return new DeadlockException(source);
            case 1586: // MysqlErrorNumbers.ER_DUP_ENTRY_WITH_KEY_NAME
            case 1062: // MysqlErrorNumbers.ER_DUP_ENTRY
                return new ConstraintViolationException(source);
            case 1146: // MysqlErrorNumbers.ER_NO_SUCH_TABLE
                return new SchemaNotFoundException(source);
            case 3024: // MysqlErrorNumbers.ER_QUERY_TIMEOUT
                return new TimeoutException(source);
            default:
                return new DatabaseException(source);
        }
    }
}
