package hex.repo;

import jill.Query;
import jill.Queryable;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Created by jason on 14-11-01.
 */
public interface Repository<T> extends Queryable<T> {
    Optional<T> find(int id);
}
