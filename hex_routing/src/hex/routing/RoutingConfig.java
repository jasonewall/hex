package hex.routing;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jason on 14-11-11.
 */
public class RoutingConfig {
    private List<Route> routes = new ArrayList<>();

    public boolean hasRoute(String path) {
        return findRouteFor(path).isPresent();
    }

    public RouteHandler getRouteHandler(String path) {
        return findRouteFor(path).map(Route::getHandler).get();
    }

    private Pattern path_params = Pattern.compile("/:(\\w+)");

    public void addRoute(String path, RouteHandler handler) {
        Route route = new Route();
        Matcher m = path_params.matcher(path);
        while(m.find()) {
            route.addParam(m.group(1));
        }
        route.setPath(Pattern.compile(m.replaceAll("/([\\\\w.]+)")));
        route.setHandler(handler);
        routes.add(route);
    }

    private Optional<Route> findRouteFor(String path) {
        return routes.stream().filter((r) -> r.matches(path)).findFirst();
    }
}
