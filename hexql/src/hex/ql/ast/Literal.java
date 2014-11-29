package hex.ql.ast;

/**
 * Created by jason on 14-10-26.
 */
public class Literal<T> implements Node {
    private T value;

    public Literal(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }
}
