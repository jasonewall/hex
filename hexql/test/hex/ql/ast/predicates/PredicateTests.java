package hex.ql.ast.predicates;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by jason on 14-12-03.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        AndPredicateTest.class,
        ConditionTest.class,
        EqualityPredicateTest.class,
        NotEqualsPredicateTest.class,
        OrPredicateTest.class
})
public class PredicateTests {
}
