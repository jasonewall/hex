package hex.ql.ast.predicates;

import hex.ql.ast.InvalidAstException;
import hex.ql.ast.Node;

import java.util.function.Predicate;

/**
 * Wrapper class for ensuring type consistency of predicates returned by
 * {@link NullPredicate#and(java.util.function.Predicate)}
 * @param <T>
 */
public class SimplePredicate<T> extends BasePredicate<T> implements Node {
    private Predicate<? super T> internalPredicate;

    public SimplePredicate(Predicate<? super T> predicate) {
        internalPredicate = predicate;
    }
    @Override
    public boolean test(T t) {
        return internalPredicate.test(t);
    }

    @Override
    public Node[] toTree() throws InvalidAstException {
        return ((Node)internalPredicate).toTree();
    }
}
