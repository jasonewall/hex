package hex.ql.ast.predicates;

import hex.ql.ast.InvalidAstException;
import hex.ql.ast.Node;
import hex.ql.ast.PropertyRef;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Created by jason on 14-10-25.
 */
public class Condition<T, R> extends BasePredicate<T> implements Predicate<T>, Node {
    private final Function<T, R> field;
    private final Predicate<R> operand;

    public Condition(Function<T, R> field, Predicate<R> operand) {
        this.field = field;
        this.operand = operand;
    }

    @Override
    public boolean test(T t) {
        return operand.test(field.apply(t));
    }

    @Override
    public Predicate<T> negate() {
        return new Condition<>(field, operand.negate());
    }

    @Override
    public Node[] toTree() throws InvalidAstException {
        if(!(operand instanceof Node))
            throw new InvalidAstException("Predicate not castable to node.");

        Node[] operandTree = ((Node) operand).toTree();

        return new Node[] {
                new PropertyRef<>(field),
                operandTree[0],
                operandTree[1]
        };
    }
}
