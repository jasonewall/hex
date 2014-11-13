package ca.thejayvm.hex.routing;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by jason on 14-11-12.
 */
public class Route {
    private Pattern pathPattern;

    private RouteHandler handler;

    private int paramIndex;

    private Map<Integer,String> params = new HashMap<>();

    public void addParam(String paramName) {
        params.put(++paramIndex, paramName);
    }

    public void setPath(Pattern pathPattern) {
        this.pathPattern = pathPattern;
    }

    public RouteHandler getHandler() {
        return handler;
    }

    public void setHandler(RouteHandler handler) {
        this.handler = handler;
    }

    public boolean matches(String path) {
        return pathPattern.matcher(path).matches();
    }
}
