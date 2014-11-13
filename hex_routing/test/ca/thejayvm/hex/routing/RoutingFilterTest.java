package ca.thejayvm.hex.routing;

import servlet_mock.HttpMock;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;
import static servlet_mock.HttpMock.*;


/**
 * Created by jason on 14-11-11.
 */
public class RoutingFilterTest {
    private RoutingFilter filter;

    @BeforeClass
    public static void initHttpMockContext() {
        HttpMock.instance().setContextPath("/blog");
    }

    @Before
    public void initFilter() {
        this.filter = new RoutingFilter();
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
}
