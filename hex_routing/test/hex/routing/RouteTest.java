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

import hex.routing.test.Post;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static servlet_mock.HttpMock.GET;

/**
 * Created by jason on 14-11-14.
 */
public class RouteTest {
    @Test
    public void shouldAllowPredicateMethodMatching() {
        Route route = new Route(HttpMethod.ANY, RoutingConfigTest.NULL_HANDLER);
        route.setPath("/posts");

        assertTrue("GET", route.matches(HttpMethod.GET, "/posts"));
        assertTrue("POST", route.matches(HttpMethod.POST, "/posts"));
        assertFalse("Wrong path", route.matches(HttpMethod.POST, "/comments"));
    }

    @Test
    public void setPathShouldDealWithPatterns() {
        Route route = new Route(RoutingConfigTest.NULL_HANDLER);
        route.setPath("/users/:username/comments");
        assertTrue(route.matches(HttpMethod.GET, "/users/albert.e/comments"));
    }

    @Test
    public void getHandlerShouldInjectRouteParams() {
        Route route = new Route(HttpMethod.ANY, "/posts/:id", RoutingConfigTest.CALLED);
        GET("/posts/100", route.getHandler()::handleRequest)
                .andThen((q,r) -> assertNotNull("RouteParams", q.getAttribute(Route.ROUTE_PARAMS)))
                ;
    }

    @Test
    public void shouldAllowTrailingSlashesInStaticRoutes() {
        Route route = new Route(HttpMethod.GET, "/people", RoutingConfigTest.CALLED);
        assertTrue(route.matches(HttpMethod.GET, "/people/"));
    }

    @Test
    public void shouldNotWorkIfYouForgetSlashes() {
        Route route = new Route(HttpMethod.ANY, "/people/:id/comments", RoutingConfigTest.UNEXPECTED);
        assertFalse(route.matches(HttpMethod.GET, "/people/13comments"));
    }

    @Test
    public void theRootRouteShouldWork() {
        // I don't know if this is a good thing but whatevs fo now.
        Stream.of("/", "").forEach(r -> {
            Route route = new Route(HttpMethod.GET, r, RoutingConfigTest.CALLED);
            Stream.of("/", "").forEach(
                    p -> assertTrue(String.format("%s -> %s", r, p), route.matches(HttpMethod.GET, p)));
        });
    }

    @Test
    public void createPathShouldReturnTheDefinedPath() {
        Route route = new Route("/aliens", RoutingConfigTest.CALLED);
        String path = route.createPath();
        assertThat(path, is("/aliens"));
    }

    @Test
    public void createPathShouldSubstituteParamsWithPassedInProperties() {
        Route route = new Route("/posts/:id", RoutingConfigTest.CALLED);
        Post post = new Post();
        post.setId(99);
        String path = route.createPath(post);
        assertThat(path, is("/posts/99"));
    }

    @Test
    public void createPathShouldWorkWithMapsAsWell() {
        Route route = new Route("/home/:profile", RoutingConfigTest.CALLED);
        Map<String,String> params = new HashMap<>();
        params.put("profile", "a.einstein");
        String path = route.createPath(params);
        assertThat(path, is("/home/a.einstein"));
    }
}
