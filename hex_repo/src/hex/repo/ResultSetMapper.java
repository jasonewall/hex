package hex.repo;

import hex.repo.metadata.DataMappingException;
import hex.repo.metadata.Metadata;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jason on 14-10-29.
 */
@FunctionalInterface
public interface ResultSetMapper<R> {
    public static <T> ResultSetMapper<List<T>> toList(ResultSetMapper<T> mapper) {
        return rs -> {
            List<T> results = new ArrayList<>();
            while(rs.next()) results.add(mapper.apply(rs));
            return results;
        };
    }

    public R apply(ResultSetWrapper rs) throws SQLException, DataMappingException;
}
