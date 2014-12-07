package hex.ql.ast.predicates;

import hex.ql.QueryLanguage;
import hex.ql.ast.InvalidAstException;
import hex.ql.ast.Node;
import hex.ql.test.Person;
import org.junit.Before;
import org.junit.Test;

import java.util.function.Predicate;

import static org.junit.Assert.*;

public class OrPredicateTest {
    private Predicate<Person> predicate;

    @Before
    public void initPredicate() {
        predicate = new OrPredicate<>(
                QueryLanguage.where(Person::getFirstName, new EqualityPredicate<>("Wayne")),
                QueryLanguage.where(Person::getFirstName, new EqualityPredicate<>("Isaac"))
        );
    }

    @Test
    public void shouldBeOkWithEitherCondition() {
        Person p = new Person();

        p.setFirstName("Wayne");
        assertTrue("Wayne yes", predicate.test(p));

        p.setFirstName("Isaac");
        assertTrue("Isaac yes", predicate.test(p));

        p.setFirstName("Fig");
        assertFalse("But not fig", predicate.test(p));
    }

    @Test
    public void negateShouldDoTheOpposite() {
        Predicate<Person> predicate = this.predicate.negate();

        Person p = new Person();

        p.setFirstName("Wayne");
        assertFalse("Wayne no", predicate.test(p));

        p.setFirstName("Isaac");
        assertFalse("Isaac no", predicate.test(p));

        p.setFirstName("Fig");
        assertTrue("But yes to fig", predicate.test(p));
    }

    @Test
    public void negateShouldBeANode() throws InvalidAstException {
        Node node = (Node) predicate.negate();
        assertNotNull(node.toTree());
    }
}
