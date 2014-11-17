package hex.routing;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jason on 14-11-12.
 */
public class Route {
    public static final String ROUTE_PARAMS = "hex.hex_routing.Route.ROUTE_PARAMS";

    private static final Pattern PATH_PARAM_DETECTOR = Pattern.compile("/:(\\w+)");

    private Pattern pathPattern;

    private RouteHandler handler;

    private int paramIndex;

    private Map<Integer,String> params = new HashMap<>();

    private Predicate<HttpMethod> methodPredicate = (m) -> m == HttpMethod.GET;

    public Route(RouteHandler handler) {
        this(HttpMethod.ANY, handler);
    }

    public Route(HttpMethod method, RouteHandler handler) {
        this(m -> m == method, handler);
    }

    public Route(Predicate<HttpMethod> methodPredicate, RouteHandler handler) {
        this.methodPredicate = methodPredicate;
        this.handler = handler;
    }

    public Route(HttpMethod method, String path, RouteHandler handler) {
        this(method, handler);
        setPath(path);
    }

    public Route(Predicate<HttpMethod> methodPredicate, String path, RouteHandler handler) {
        this(methodPredicate, handler);
        setPath(path);
    }

    public void setPath(Pattern pathPattern) {
        this.pathPattern = pathPattern;
    }

    public void setPath(String path) {
        Matcher m = PATH_PARAM_DETECTOR.matcher(path);
        while(m.find()) {
            addParam(m.group(1));
        }
        setPath(Pattern.compile(m.replaceAll("/([\\\\w.-]+)")));
    }

    public RouteHandler getHandler() {
        return (q, r) -> {
            HttpServletRequest request = (HttpServletRequest) q;
            String contextUri = request.getRequestURI().replaceFirst(request.getContextPath(), "");
            q.setAttribute(ROUTE_PARAMS, new RouteParams(getParamValues(contextUri)));
            handler.handleRequest(q, r);
        };
    }

    public boolean matches(HttpMethod method, String path) {
        return methodPredicate.test(method) && pathPattern.matcher(path).matches();
    }

    private void addParam(String paramName) {
        params.put(++paramIndex, paramName);
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
