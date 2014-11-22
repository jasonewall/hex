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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static hex.routing.HttpMethod.GET;

/**
 * Created by jason on 14-11-11.
 */
public class RoutingConfigBase implements RoutingConfig {
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

    public void addRoute(String path, RouteHandler handler) {
        addRoute(GET, path, handler);
    }

    public void addRoute(HttpMethod method, String path, RouteHandler handler) {
        routes.add(new Route(method, path, handler));
    }

    public void addRoute(Route route) {
        routes.add(route);
    }

    private Optional<Route> findRouteFor(HttpMethod method, String path) {
        return routes.stream().filter((r) -> r.matches(method, path)).findFirst();
    }
}
