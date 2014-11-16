/*
 * Copyright 2014 Jason E. Wall
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
    public void shouldDifferentiateBetweenMethods() {
        config.addRoute(HttpMethod.POST, "/articles", UNEXPECTED);
        assertTrue("Has POST route", config.hasRoute(HttpMethod.POST, "/articles"));
    }

    public static RouteParams getRouteParams(ServletRequest request) {
        return (RouteParams) request.getAttribute(Route.ROUTE_PARAMS);
    }
}
