package ca.thejayvm.hex.routing;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jason on 14-11-11.
 */
public class RoutingConfig {
    private Map<String,RouteHandler> handlers = new HashMap<>();

    public boolean hasRoute(String path) {
        return handlers.containsKey(path);
    }

    public RouteHandler getRouteHandler(String path) {
        return handlers.get(path);
    }

    public void addRoute(String path, RouteHandler handler) {
        handlers.put(path, handler);
    }
}
