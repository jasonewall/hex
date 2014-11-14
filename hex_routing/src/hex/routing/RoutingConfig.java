package hex.routing;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static hex.routing.HttpMethod.GET;

/**
 * Created by jason on 14-11-11.
 */
public class RoutingConfig {
    private List<Route> routes = new ArrayList<>();

    public boolean hasRoute(String path) {
        return hasRoute(GET, path);
    }

    public boolean hasRoute(String method, String path) {
        return hasRoute(HttpMethod.valueOf(method.toUpperCase()), path);
    }

    public boolean hasRoute(HttpMethod method, String path) {
        return findRouteFor(method, path).isPresent();
    }

    public RouteHandler getRouteHandler(String path) {
        return getRouteHandler(GET, path);
    }

    public RouteHandler getRouteHandler(String method, String path) {
        return getRouteHandler(HttpMethod.valueOf(method.toUpperCase()), path);
    }

    public RouteHandler getRouteHandler(HttpMethod method, String path) {
        return findRouteFor(method, path).map(Route::getHandler).get();
    }

    private Pattern path_params = Pattern.compile("/:(\\w+)");

    public void addRoute(String path, RouteHandler handler) {
        addRoute(GET, path, handler);
    }

    public void addRoute(HttpMethod method, String path, RouteHandler handler) {
        Route route = new Route(method, handler);
        Matcher m = path_params.matcher(path);
        while(m.find()) {
            route.addParam(m.group(1));
        }
        route.setPath(Pattern.compile(m.replaceAll("/([\\\\w.-]+)")));
        routes.add(route);
    }

    private Optional<Route> findRouteFor(HttpMethod method, String path) {
        return routes.stream().filter((r) -> r.matches(method, path)).findFirst();
    }
}
