package hex.ql.ast;

/**
 * Created by jason on 14-10-26.
 */
public interface Node {
    default public Node[] toTree() throws InvalidAstException {
        return null;
    }
}
