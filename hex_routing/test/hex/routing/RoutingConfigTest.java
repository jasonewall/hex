package hex.routing;

import org.junit.Test;

import javax.servlet.ServletRequest;

import static org.junit.Assert.*;
import static servlet_mock.HttpMock.*;

/**
 * Created by jason on 14-11-11.
 */
public class RoutingConfigTest {
    @Test
    public void staticRoutes() {
        RoutingConfig config = new RoutingConfig();
        config.addRoute("/people", (q, r) -> q.setAttribute("Boring", "Boring"));
        RouteHandler handler = config.getRouteHandler("/people");
        assertNotNull("Should retrieve a static path", handler);
        GET("/people", handler::handleRequest)
                .andThen((q, r) -> assertEquals("Boring", q.getAttribute("Boring")))
                ;
    }

    @Test
    public void dynamicRouteWithNamedParam() {
        RoutingConfig config = new RoutingConfig();
        config.addRoute("/posts/:id", (q, r) -> q.setAttribute("post_id", getRouteParams(q).getInt("id")));
        RouteHandler handler = config.getRouteHandler("/posts/7");
        assertNotNull("Should retrieve paramed path", handler);
        GET("/posts/7", handler::handleRequest)
                .andThen((q, r) -> assertEquals(7, q.getAttribute("post_id")))
                ;
    }

    @Test
    public void nestedRouteWithNamedParams() {
        RoutingConfig config = new RoutingConfig();
        config.addRoute("/posts/:post_id/comments/:id", (q, r) -> {
            RouteParams routeParams = getRouteParams(q);
            q.setAttribute("post_id", routeParams.getInt("post_id"));
            q.setAttribute("comment_id", routeParams.getInt("id"));
        });
        RouteHandler handler = config.getRouteHandler("/posts/99/comments/388");
        assertNotNull("Should retrieve handler", handler);
        GET("/posts/99/comments/373", handler::handleRequest)
                .andThen((q, r) -> assertEquals(99, q.getAttribute("post_id")))
                .andThen((q, r) -> assertEquals(373, q.getAttribute("comment_id")))
                ;
    }

    private RouteParams getRouteParams(ServletRequest request) {
        return (RouteParams) request.getAttribute(Route.ROUTE_PARAMS);
    }
}
