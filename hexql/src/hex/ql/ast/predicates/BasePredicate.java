package hex.ql.ast.predicates;

import java.util.function.Predicate;

/**
 * Created by jason on 14-10-26.
 */
public abstract class BasePredicate<T> implements Predicate<T> {
    @Override
    public Predicate<T> and(Predicate<? super T> other) {
        return new AndPredicate<>(this, other);
    }

    @Override
    public Predicate<T> or(Predicate<? super T> other) {
        return new OrPredicate<>(this, other);
    }
}
