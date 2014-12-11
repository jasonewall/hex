package hex.action;

import hex.action.params.ParamBaseTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by jason on 14-11-15.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        ControllerActionTest.class,
        RouteManagerTest.class,
        ControllerTest.class,
        ParamBaseTest.class
})
public class ActionTests {
}
