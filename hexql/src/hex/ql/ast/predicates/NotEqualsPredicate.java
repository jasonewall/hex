package hex.ql.ast.predicates;

import hex.ql.ast.Comparator;
import hex.ql.ast.InvalidAstException;
import hex.ql.ast.Literal;
import hex.ql.ast.Node;

import java.util.function.Predicate;

/**
 * Created by jason on 14-12-03.
 */
public class NotEqualsPredicate<T> extends ValuePredicate<T> implements Node {

    public NotEqualsPredicate(T value) {
        super(value);
    }

    @Override
    public boolean test(T t) {
        return !value.equals(t);
    }

    @Override
    public Predicate<T> negate() {
        return new EqualityPredicate<>(value);
    }

    @Override
    public Node[] toTree() throws InvalidAstException {
        return new Node[]{
                new Comparator(Comparator.Type.NotEquals),
                new Literal<T>(value)
        };
    }
}
