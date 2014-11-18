package hex.repo;

import hex.ql.Queryable;

import java.util.Optional;

/**
 * Created by jason on 14-11-01.
 */
public interface Repository<T> extends Queryable<T> {
    Optional<T> find(int id);
}
