package hex.ql.ast.predicates;

import hex.ql.QueryLanguage;
import hex.ql.ast.InvalidAstException;
import hex.ql.ast.Node;
import hex.ql.test.Person;
import org.junit.Before;
import org.junit.Test;

import java.util.function.Predicate;

import static org.junit.Assert.*;

public class AndPredicateTest {
    private AndPredicate<Person> predicate;

    @Before
    public void initPredicate() {
        predicate = new AndPredicate<>(
                QueryLanguage.where(Person::getFirstName, new EqualityPredicate<>("Isaac")),
                QueryLanguage.where(Person::getLastName, new EqualityPredicate<>("Newton"))
        );
    }
    @Test
    public void testShouldCheckBothPredicates() {
        Person p = new Person();
        p.setFirstName("Wayne");
        p.setLastName("Gretzky");
        assertFalse("Both fail", predicate.test(p));

        p.setFirstName("Isaac");
        assertFalse("First name matches", predicate.test(p));

        p.setLastName("Newton");
        assertTrue("Both match", predicate.test(p));

        p.setFirstName("Fig");
        assertFalse("Last name matches", predicate.test(p));
    }

    @Test
    public void negateShouldDoAllOpposites() {
        Predicate<Person> predicate = this.predicate.negate();
        Person p = new Person();
        p.setFirstName("Wayne");
        p.setLastName("Gretzky");
        assertTrue("Both fail", predicate.test(p));

        p.setFirstName("Isaac");
        assertTrue("First name matches", predicate.test(p));

        p.setLastName("Newton");
        assertFalse("Both match", predicate.test(p));

        p.setFirstName("Fig");
        assertTrue("Last name matches", predicate.test(p));
    }

    @Test
    public void negatedShouldBeASTNodes() throws InvalidAstException {
        Node node = (Node) predicate.negate();
        Node[] tree = node.toTree();
        assertNotNull(tree);
    }
}
