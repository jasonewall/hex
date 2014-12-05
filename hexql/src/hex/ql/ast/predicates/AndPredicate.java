package hex.ql.ast.predicates;

import hex.ql.ast.BooleanOperator;
import hex.ql.ast.InvalidAstException;
import hex.ql.ast.Node;

import java.util.function.Predicate;

/**
 * Created by jason on 14-10-26.
 */
public class AndPredicate<T> extends CompoundPredicate<T> implements Predicate<T>, Node {

    public AndPredicate(Predicate<T> lhs, Predicate<? super T> rhs) {
        super(lhs, rhs);
    }

    @Override
    public boolean test(T t) {
        return lhs.test(t) && rhs.test(t);
    }

    @Override
    public Predicate<T> negate() {
        return new OrPredicate<>(lhs.negate(), rhs.negate());
    }

    @Override
    public Node[] toTree() throws InvalidAstException {
        return new Node[] {
                getLhNode(),
                BooleanOperator.And,
                getRhNode()
        };
    }
}
