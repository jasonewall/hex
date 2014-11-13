package ca.thejayvm.hex.routing;

import org.junit.Test;
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
                .andThen((q, r) -> assertEquals("Boring", q.getAttribute("Boring")));
    }
}
