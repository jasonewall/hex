package ca.thejayvm.hex.routing;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by jason on 14-11-11.
 */
public class RoutingConfig {
    public boolean hasRoute(ServletRequest servletRequest) {
        return hasRoute(getPathInfo(servletRequest));
    }

    public boolean hasRoute(String path) {
        return false;
    }

    public RouteHandler getRouteHandler(ServletRequest servletRequest) {
        return getRouteHandler(getPathInfo(servletRequest));
    }

    public RouteHandler getRouteHandler(String path) {
        return null;
    }

    private String getPathInfo(ServletRequest servletRequest) {
        return ((HttpServletRequest) servletRequest).getPathInfo();
    }
}
