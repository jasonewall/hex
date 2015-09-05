package hex.action.views.jsp.tags;

import hex.action.Application;
import hex.action.routing.RouteManager;
import org.junit.Before;
import org.junit.Test;
import servlet_mock.jsp.MockJspContext;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by jason on 15-01-24.
 */
public class LinkTagTest {
    private RouteManager routes = new RouteManager();

    private LinkTag tag;

    @Before
    public void setUpTag() {
        tag = new LinkTag();
    }

    @Test
    public void linksShouldRenderQueryParams() {

    }

    private void setUpRequest(HttpServletRequest request, HttpServletResponse response) {
        setUpRoutes(request.getServletContext());
        tag.setPageContext(new MockJspContext(request));

    }

    private void setUpRoutes(ServletContext servletContext) {
        servletContext.setAttribute(Application.ROUTING_CONFIG_CLASSES, new String[]{ LinkTagTest.class.getName() });
        servletContext.setAttribute(LinkTagTest.class.getName(), routes);
    }
}
