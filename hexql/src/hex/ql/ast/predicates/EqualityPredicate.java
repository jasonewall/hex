package hex.ql.ast.predicates;

import hex.ql.ast.Comparator;
import hex.ql.ast.Literal;
import hex.ql.ast.Node;

import java.util.function.Predicate;

/**
 * Created by jason on 14-10-26.
 */
public class EqualityPredicate<T> extends BasePredicate<T> implements Predicate<T>, Node {
    private final T value;

    public EqualityPredicate(T value) {
        this.value = value;
    }

    @Override
    public boolean test(T t) {
        return value.equals(t);
    }

    @Override
    public Node[] toTree() {
        return new Node[] {
                new Comparator(Comparator.Type.Equals),
                new Literal<T>(value)
        };
    }
}
