package hex.ql.ast;

/**
 * Created by jason on 14-10-26.
 */
public class BooleanOperator implements Node {
    public enum Type {
        And,
        Or,
    }

    public static final BooleanOperator And = new BooleanOperator(Type.And);
    public static final BooleanOperator Or = new BooleanOperator(Type.Or);

    private Type type;

    public BooleanOperator(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }
}
