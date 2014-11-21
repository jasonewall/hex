package hex.repo;

import hex.repo.metadata.DataMappingException;

import java.sql.SQLException;

/**
 * Created by jason on 14-10-29.
 */
@FunctionalInterface
public interface ResultSetMapper<R> {
    public R apply(ResultSetWrapper rs) throws SQLException, DataMappingException;
}
