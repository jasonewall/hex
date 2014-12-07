package hex.ql.ast.predicates;

/**
 * Created by jason on 14-12-03.
 */
public abstract class ValuePredicate<T> extends BasePredicate<T> {
    protected final T value;

    public ValuePredicate(T value) {
        this.value = value;
    }
}
