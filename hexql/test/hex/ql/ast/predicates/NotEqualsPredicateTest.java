package hex.ql.ast.predicates;

import hex.ql.ast.Comparator;
import hex.ql.ast.InvalidAstException;
import hex.ql.ast.Literal;
import hex.ql.ast.Node;
import org.junit.Test;

import java.util.function.Predicate;

import static org.junit.Assert.*;

public class NotEqualsPredicateTest {
    @Test
    public void shouldReturnFalseIfTestMatchesValue() {
        NotEqualsPredicate<String> p = new NotEqualsPredicate<>("Fig");
        assertFalse(p.test("Fi"));
    }

    @Test
    public void toTreeShouldRenderNotEqualsOperator() throws InvalidAstException {
        NotEqualsPredicate<String> p = new NotEqualsPredicate<>("Wayne");
        Node[] tree = p.toTree();
        assertEquals("Comparator", Comparator.Type.NotEquals, ((Comparator) tree[0]).getType());
        assertEquals("Value", "Wayne", ((Literal) tree[1]).getValue());
    }

    @Test
    public void negateShouldConvertIntoEqualityPredicate() throws InvalidAstException {
        Predicate<String> p = new NotEqualsPredicate<>("Fig");
        assertFalse("Before negate", p.test("Fig"));

        p = p.negate();
        assertTrue("After negate", p.test("Fig"));

        Node[] tree = ((Node) p).toTree();
        assertEquals("Comparator", Comparator.Type.Equals, ((Comparator) tree[0]).getType());
    }
}
