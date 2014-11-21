package hex.repo;

/**
 * Created by jason on 14-10-29.
 */
@FunctionalInterface
public interface ResultSetMapper<R> {
    public R apply(ResultSetWrapper rs) throws java.sql.SQLException;
}
