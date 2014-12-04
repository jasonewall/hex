package hex.ql.ast.predicates;

import hex.utils.Null;

import java.util.function.Predicate;

/**
 * Created by jason on 14-10-25.
 */
public class NullPredicate<T> extends Null.Predicate<T> {
    @Override
    public Predicate<T> and(Predicate<? super T> other) {
        return new SimplePredicate<>(other);
    }
}
