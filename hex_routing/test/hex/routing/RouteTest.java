package hex.routing;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.regex.Pattern;


/**
 * Created by jason on 14-11-14.
 */
public class RouteTest {
    @Test
    public void shouldAllowPredicateMethodMatching() {
        Route route = new Route(HttpMethod.ANY, RoutingConfigTest.NULL_HANDLER);
        route.setPath(Pattern.compile("/posts"));

        assertTrue("GET", route.matches(HttpMethod.GET, "/posts"));
        assertTrue("POST", route.matches(HttpMethod.POST, "/posts"));
        assertFalse("Wrong path", route.matches(HttpMethod.POST, "/comments"));
    }

    @Test
    public void setPathShouldDealWithPatterns() {
        Route route = new Route(RoutingConfigTest.NULL_HANDLER);
        route.setPath("/users/:username/comments");
        assertTrue(route.matches(HttpMethod.GET, "/users/albert.e/comments"));
    }
}
