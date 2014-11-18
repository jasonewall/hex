package hex.ql.ast.predicates;

import java.util.function.Predicate;

/**
 * Created by jason on 14-10-25.
 */
public class NullPredicate<T> implements Predicate<T> {
    @Override
    public boolean test(T t) {
        return true;
    }

    @Override @SuppressWarnings("unchecked")
    public Predicate<T> and(Predicate<? super T> other) {
        return (Predicate<T>) other;
    }

    @Override
    public Predicate<T> negate() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Predicate<T> or(Predicate<? super T> other) {
        throw new UnsupportedOperationException();
    }
}
