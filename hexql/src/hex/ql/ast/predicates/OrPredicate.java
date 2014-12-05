package hex.ql.ast.predicates;

import hex.ql.ast.BooleanOperator;
import hex.ql.ast.InvalidAstException;
import hex.ql.ast.Node;

import java.util.function.Predicate;

/**
 * Created by jason on 14-10-26.
 */
public class OrPredicate<T> extends CompoundPredicate<T> implements Predicate<T>, Node {
    public OrPredicate(Predicate<T> lhs, Predicate<? super T> rhs) {
        super(lhs, rhs);
    }

    @Override
    public boolean test(T t) {
        return lhs.test(t) || rhs.test(t);
    }

    @Override
    public Node[] toTree() throws InvalidAstException {
        return new Node[] {
                getLhNode(),
                BooleanOperator.Or,
                getRhNode()
        };
    }
}
