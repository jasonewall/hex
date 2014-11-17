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

import org.junit.Before;
import org.junit.Test;

import javax.servlet.ServletRequest;

import java.util.Arrays;

import static org.junit.Assert.*;
import static servlet_mock.HttpMock.*;

/**
 * Created by jason on 14-11-11.
 */
public class RoutingConfigTest {
    final static RouteHandler CALLED = (q, r) -> q.setAttribute("Called", true);
    private RoutingConfig config;

    public static final RouteHandler UNEXPECTED = (q, r) -> {
        throw new RuntimeException("Dummy handler did not expect your call.");
    };

    public static final RouteHandler NULL_HANDLER = (q, r) -> {};

    @Before
    public void setupRoutingConfig() {
        this.config = new RoutingConfig();
    }
    @Test
    public void staticRoutes() {
        config.addRoute("/people", (q, r) -> q.setAttribute("Boring", "Boring"));
        RouteHandler handler = config.getRouteHandler("/people");
        assertNotNull("Should retrieve a static path", handler);
        GET("/people", handler::handleRequest)
                .andThen((q, r) -> assertEquals("Boring", q.getAttribute("Boring")))
                ;
    }

    @Test
    public void dynamicRouteWithNamedParam() {
        config.addRoute("/posts/:id", (q, r) -> q.setAttribute("post_id", getRouteParams(q).getInt("id")));
        RouteHandler handler = config.getRouteHandler("/posts/7");
        assertNotNull("Should retrieve paramed path", handler);
        GET("/posts/7", handler::handleRequest)
                .andThen((q, r) -> assertEquals(7, q.getAttribute("post_id")))
                ;
    }

    @Test
    public void nestedRouteWithNamedParams() {
        config.addRoute("/posts/:post_id/comments/:id", (q, r) -> {
            RouteParams routeParams = getRouteParams(q);
            q.setAttribute("post_id", routeParams.getInt("post_id"));
            q.setAttribute("comment_id", routeParams.getInt("id"));
        });
        RouteHandler handler = config.getRouteHandler("/posts/99/comments/388");
        assertNotNull("Should retrieve handler", handler);
        GET("/posts/99/comments/373", handler::handleRequest)
                .andThen((q, r) -> assertEquals(99, q.getAttribute("post_id")))
                .andThen((q, r) -> assertEquals(373, q.getAttribute("comment_id")))
                ;
    }

    @Test
    public void stringRouteParams() {
        config.addRoute("/profile/:username", (q, r) -> assertEquals("isaac.newton", getRouteParams(q).getString("username")));

        GET("/profile/isaac.newton", config.getRouteHandler("/profile/isaac.newton")::handleRequest);
    }

    @Test
    public void shouldNotAllowWeirdCharacters() {
        config.addRoute("/profile/:username", UNEXPECTED);
        Arrays.asList(
                "isaac,newton", // no real attachment to either of these being illegal characters
                "marsha+brady"  // can be moved to the allowed list if desired
        ).forEach((s) -> assertFalse(s, config.hasRoute("/profile/".concat(s))));
    }

    @Test
    public void shouldProbablyAllowSEOFriendlyCharacters() {
        config.addRoute("/article/:article_key", NULL_HANDLER);
        Arrays.asList(
                "man-lands-on-the-moon"
        ).forEach((s) -> assertTrue(s, config.hasRoute("/article/".concat(s))));
    }

    @Test
    public void shouldAllowTrailingSlashes() {
        config.addRoute("/articles/:id", CALLED);
        GET("/articles/88/", config.getRouteHandler("/articles/88/")::handleRequest)
                .andThen((q,r) -> assertTrue("Called", (boolean)q.getAttribute("Called")));
    }

    @Test
    public void shouldDifferentiateBetweenMethods() {
        config.addRoute(HttpMethod.POST, "/articles", UNEXPECTED);
        assertTrue("Has POST route", config.hasRoute(HttpMethod.POST, "/articles"));
    }

    public static RouteParams getRouteParams(ServletRequest request) {
        return (RouteParams) request.getAttribute(Route.ROUTE_PARAMS);
    }
}
