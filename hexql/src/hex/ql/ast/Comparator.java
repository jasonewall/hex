package hex.ql.ast;

/**
 * Created by jason on 14-10-26.
 */
public class Comparator implements Node {
    public enum Type {
        Equals
    }

    private Type type;

    public Comparator(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }
}
