package hex.ql.ast.predicates;

import hex.ql.ast.InvalidAstException;
import hex.ql.ast.Node;
import hex.ql.test.Person;
import org.junit.Test;

import java.util.function.Predicate;

import static org.junit.Assert.*;

public class ConditionTest {
    @Test
    public void negateShouldReturnAnOppositeCondition() {
        Person wayne = new Person();
        wayne.setFirstName("Wayne");

        Person isaac = new Person();
        isaac.setFirstName("Isaac");

        Condition<Person,String> condition = new Condition<>(Person::getFirstName, new EqualityPredicate<>("Wayne"));
        assertTrue("Matches before", condition.test(wayne));
        assertFalse("Non-match before", condition.test(isaac));

        Predicate<Person> negated = condition.negate();
        assertFalse("Matches after", negated.test(wayne));
        assertTrue("Non-match after", negated.test(isaac));
    }

    @Test
    public void negateResultShouldBeANode() throws InvalidAstException {
        Condition<Person,String> condition = new Condition<>(Person::getLastName, new EqualityPredicate<>("Newton"));
        Node node = (Node)condition.negate();
        assertNotNull(node.toTree());
    }
}
