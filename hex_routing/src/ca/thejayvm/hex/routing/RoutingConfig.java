package ca.thejayvm.hex.routing;

/**
 * Created by jason on 14-11-11.
 */
public class RoutingConfig {
    public boolean hasRoute(String path) {
        return false;
    }

    public RouteHandler getRouteHandler(String path) {
        return null;
    }

    public void addRoute(String path, RouteHandler handler) {

    }
}
