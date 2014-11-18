package hex.ql.ast.predicates;

import static org.junit.Assert.*;

import hex.ql.ast.Comparator;
import hex.ql.ast.Literal;
import hex.ql.ast.Node;
import org.junit.Test;

/**
 * Created by jason on 14-10-26.
 */
public class EqualityPredicateTest {
    @Test
    public void toTreeShouldReturnThings() {
        EqualityPredicate<String> predicate = new EqualityPredicate<>("Jason");
        Node[] ast = predicate.toTree();
        assertEquals(Comparator.Type.Equals, ((Comparator) ast[0]).getType());
        assertEquals("Jason", ((Literal) ast[1]).getValue());
    }
}
