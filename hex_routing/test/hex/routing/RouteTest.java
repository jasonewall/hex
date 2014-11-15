package hex.routing;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.regex.Pattern;

import static servlet_mock.HttpMock.*;


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

    @Test
    public void getHandlerShouldInjectRouteParams() {
        Route route = new Route(HttpMethod.ANY, "/posts/:id", RoutingConfigTest.CALLED);
        GET("/posts/100", route.getHandler()::handleRequest)
                .andThen((q,r) -> assertNotNull("RouteParams", q.getAttribute(Route.ROUTE_PARAMS)))
                ;
    }
}
