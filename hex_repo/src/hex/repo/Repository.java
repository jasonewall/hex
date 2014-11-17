package hex.repo;

import jill.Queryable;

import java.util.Optional;
import java.util.function.Predicate;

/**
 * Created by jason on 14-11-01.
 */
public interface Repository<T> extends Queryable<T> {
    public Optional<T> find(int id);

    public Queryable<T> where(Predicate<? super T> predicate);
}
