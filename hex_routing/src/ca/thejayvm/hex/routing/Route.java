package ca.thejayvm.hex.routing;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jason on 14-11-12.
 */
public class Route {
    public static final String ROUTE_PARAMS = "hex.hex_routing.Route.ROUTE_PARAMS";

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
        return (q, r) -> {
            HttpServletRequest request = (HttpServletRequest) q;
            String contextUri = request.getRequestURI().replaceFirst(request.getContextPath(), "");
            q.setAttribute(ROUTE_PARAMS, new RouteParams(getParamValues(contextUri)));
            handler.handleRequest(q, r);
        };
    }

    public void setHandler(RouteHandler handler) {
        this.handler = handler;
    }

    public boolean matches(String path) {
        return pathPattern.matcher(path).matches();
    }

    private Map<String,String> getParamValues(String path) {
        Matcher m = pathPattern.matcher(path);
        Map<String,String> paramValues = new HashMap<>(m.groupCount());
        if(m.matches()) {
            for (int i = 1; i <= m.groupCount(); i++) {
                paramValues.put(params.get(i), m.group(i));
            }
        }

        return paramValues;
    }
}
