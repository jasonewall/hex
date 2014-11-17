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
import org.junit.BeforeClass;
import org.junit.Test;
import servlet_mock.HttpMock;
import servlet_mock.HttpServletRequestHandler;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import java.io.IOException;

import static hex.routing.RoutingConfigTest.getRouteParams;
import static org.junit.Assert.*;
import static servlet_mock.HttpMock.GET;
import static servlet_mock.HttpMock.POST;


/**
 * Created by jason on 14-11-11.
 */
public class RoutingFilterTest {
    private RoutingFilter filter;

    private final RoutingConfig routingConfig = new RoutingConfig();

    @BeforeClass
    public static void initHttpMockContext() {
        HttpMock.instance().setContextPath("/blog");
    }

    @Before
    public void initFilter() {
        this.filter = new RoutingFilter();
        filter.setRoutingConfig(routingConfig);
    }

    @Test
    public void getPathShouldRemoveTheContextPathFromRequestURI() {
        GET("/posts", (q, r) -> assertEquals("/posts", filter.getPath(q)));
    }

    @Test
    public void getPathShouldWorkInTheRootContext() {
        HttpMock.instance().setContextPath("");
        GET("/people", (q, r) -> {
            assertEquals("/people", q.getRequestURI());
            assertEquals("/people", filter.getPath(q));
        });
    }

    @Test
    public void filterShouldDifferentiateBetweenMethods() {
        routingConfig.addRoute("/articles", RoutingConfigTest.UNEXPECTED);

        POST("/articles", doFilter((q, r) -> q.setAttribute("Called", true)))
                .andThen((q, r) -> assertTrue("Delegated to filter chain", (boolean) q.getAttribute("Called")))
                ;
    }

    @Test
    public void filterShouldDelegateToHandler() {
        routingConfig.addRoute("/books", RoutingConfigTest.CALLED);

        GET("/books", doFilter((q,r) -> fail("Expected filter to handle request.")))
                .andThen((q,r) -> assertTrue("Filter handled request", (boolean)q.getAttribute("Called")))
                ;
    }

    @Test
    public void filterShouldDelegatePOSTToHandler() {
        routingConfig.addRoute(HttpMethod.POST, "/posts/:id/comments", (q,r) -> q.setAttribute("post_id", getRouteParams(q).getInt("id")));

        POST("/posts/17/comments", doFilter((q,r) -> fail("Expected filter to handle POST request")))
                .andThen((q,r) -> assertEquals(17, q.getAttribute("post_id")))
                ;
    }

    private HttpServletRequestHandler doFilter(FilterChain filterChain) {
        return (q, r) -> {
            try {
                filter.doFilter(q, r, filterChain);
            } catch (ServletException | IOException e) {
                fail(e.getMessage());
                e.printStackTrace();
            }
        };
    }
}
