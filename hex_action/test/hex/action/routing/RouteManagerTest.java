package hex.action.routing;

import hex.action.examples.PostsController;
import hex.action.routing.RouteManager;
import hex.routing.HttpMethod;
import hex.routing.Route;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import java.util.stream.Stream;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by jason on 14-11-15.
 */
public class RouteManagerTest {
    private RouteManager routeManager;

    @Before
    public void initRouteManager() {
        this.routeManager = new RouteManager();
    }

    @Test
    public void getShouldCreateAGetRoute() {
        String path = "/posts";
        routeManager.get(path, PostsController::new, "index");
        Route route = routeManager.getDefinedRoutes().get(0);
        assertTrue(route.matches(HttpMethod.GET, path));
        assertFalse(route.matches(HttpMethod.POST, path));
    }

    @Test
    public void postShouldCreateAPostRoute() {
        String path = "/plants";
        routeManager.post(path, PostsController::new, "create");
        Route route = routeManager.getDefinedRoutes().get(0);
        assertTrue(route.matches(HttpMethod.POST, path));
        assertFalse(route.matches(HttpMethod.GET, path));
    }

    @Test
    public void putShouldCreateAPutRoute() {
        String path = "/aliens";
        routeManager.put(path, PostsController::new, "report_to_the_mother_ship");
        Route route = routeManager.getDefinedRoutes().get(0);
        assertTrue(route.matches(HttpMethod.PUT, path));
        assertFalse(route.matches(HttpMethod.GET, path));
    }

    @Test
    public void deleteShouldCreateADeleteRoute() {
        String path = "/aliens";
        routeManager.delete(path, PostsController::new, "go_home_please");
        Route route = routeManager.getDefinedRoutes().get(0);
        assertTrue(route.matches(HttpMethod.DELETE, path));
        assertFalse(route.matches(HttpMethod.POST, path));
    }

    @Test
    public void matchesShouldMatchAnyOldMethod() {
        String path = "/users";
        routeManager.matches(path, PostsController::new, "users");
        Route route = routeManager.getDefinedRoutes().get(0);
        Stream.of(HttpMethod.values()).forEach(
                m -> assertTrue(m.toString(), route.matches(m, path))
        );
    }

    @Test
    public void addingRoutesShouldCreatePathNames() {
        String path = "/aliens";
        routeManager.get(path, PostsController::new, "report_to_the_mother_ship");
        Route route = routeManager.getRouteNamed("report_to_the_mother_ship_aliens");
        assertThat(route, notNullValue());
    }
}
