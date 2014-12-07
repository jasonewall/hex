package hex.ql.ast.predicates;

import static org.junit.Assert.*;

import hex.ql.ast.Comparator;
import hex.ql.ast.InvalidAstException;
import hex.ql.ast.Literal;
import hex.ql.ast.Node;
import org.junit.Test;

/**
 * Created by jason on 14-10-26.
 */
public class EqualityPredicateTest {
    @Test
    public void toTreeShouldReturnThings() {
        EqualityPredicate<String> predicate = new EqualityPredicate<>("Isaac");
        Node[] ast = predicate.toTree();
        assertEquals(Comparator.Type.Equals, ((Comparator) ast[0]).getType());
        assertEquals("Isaac", ((Literal) ast[1]).getValue());
    }

    @Test
    public void negateShouldReturnANotEqualsAST() throws InvalidAstException {
        EqualityPredicate<String> predicate = new EqualityPredicate<>("Wayne");
        Node node = (Node) predicate.negate();
        Node[] ast = node.toTree();
        assertEquals(Comparator.Type.NotEquals, ((Comparator) ast[0]).getType());
        assertEquals("Wayne", ((Literal) ast[1]).getValue());
    }
}
