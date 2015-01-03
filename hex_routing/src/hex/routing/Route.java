/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Jason E. Wall
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
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

    public static final Pattern PATH_PARAM_PATTERN = Pattern.compile("/:(\\w+)");

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

    private void setPattern(Pattern pathPattern) {
        this.pathPattern = pathPattern;
    }

    public void setPath(String path) {
        Matcher m = PATH_PARAM_PATTERN.matcher(path);
        while(m.find()) {
            addParam(m.group(1));
        }
        String routePattern = m.replaceAll("/([\\\\w.-]+)");
        if(routePattern.endsWith("/")) {
            routePattern = routePattern.substring(0, routePattern.length() - 1);
        }
        setPattern(Pattern.compile(routePattern + "[/]?"));
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
