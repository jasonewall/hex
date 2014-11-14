package hex.repo;

/**
 * Created by jason on 14-10-29.
 */
@FunctionalInterface
public interface SQLFunction<T,R> {
    public R apply(T t) throws java.sql.SQLException;
}
