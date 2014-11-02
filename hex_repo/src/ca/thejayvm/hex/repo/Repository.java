package ca.thejayvm.hex.repo;

import ca.thejayvm.jill.Query;
import ca.thejayvm.jill.Queryable;

import java.util.function.Predicate;

/**
 * Created by jason on 14-11-01.
 */
public interface Repository<T> extends Queryable<T> {
    public Queryable<T> where(Predicate<T> predicate);
}
