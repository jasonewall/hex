package hex.action;

import hex.action.params.ParamsSuite;
import hex.action.routing.RoutingSuite;
import hex.action.views.ViewsSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by jason on 14-11-15.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        ControllerActionTest.class,
        RoutingSuite.class,
        ControllerTest.class,
        ParamsSuite.class,
        ViewsSuite.class
})
public class ActionTests {
}
