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

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.regex.Pattern;

import static servlet_mock.HttpMock.*;

/**
 * Created by jason on 14-11-14.
 */
public class RouteTest {
    @Test
    public void shouldAllowPredicateMethodMatching() {
        Route route = new Route(HttpMethod.ANY, RoutingConfigTest.NULL_HANDLER);
        route.setPath(Pattern.compile("/posts"));

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
}
