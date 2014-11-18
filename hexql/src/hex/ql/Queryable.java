package hex.ql;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Created by jason on 14-10-25.
 *
 * An interface that describes an entity that can have a query performed on it.
 */
public interface Queryable<T> extends Iterable<T> {
    public Query<T> stream();

    default <R> Query<T> where(Function<T, R> extractor, Predicate<R> operand) {
        return stream().where(extractor, operand);
    }
}
