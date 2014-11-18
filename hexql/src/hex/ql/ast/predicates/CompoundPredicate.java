package hex.ql.ast.predicates;

import hex.ql.ast.InvalidAstException;
import hex.ql.ast.Node;

import java.util.function.Predicate;

/**
 * Created by jason on 14-10-26.
 */
public abstract class CompoundPredicate<T> extends BasePredicate<T> {
    protected final Predicate<T> lhs;
    protected final Predicate<? super T> rhs;

    public CompoundPredicate(Predicate<T> lhs, Predicate<? super T> rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    protected Node getLhNode() throws InvalidAstException {
        if(!(lhs instanceof Node))
            throw new InvalidAstException("lhs predicate cannot be converted to AST");
        return (Node)lhs;
    }

    protected Node getRhNode() throws InvalidAstException {
        if(!(rhs instanceof Node))
            throw new InvalidAstException("rhs predicate cannot be converted to AST");
        return (Node)rhs;
    }
}
