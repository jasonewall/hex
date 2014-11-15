package hex.action;

import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import static org.junit.Assert.*;
import static servlet_mock.HttpMock.GET;

/**
 * Created by jason on 14-11-15.
 */
public class ControllerActionTest {
    private ControllerAction action;

    private ServletRequest servletRequest;

    private ServletResponse servletResponse;

    private ServletException servletException;

    @SuppressWarnings("UnusedDeclaration")
    class ControllerActionTestController extends Controller {
        public void setCalled() {
            viewContext.put("CALLED", true);
        }

        private void tryPrivateAction() {
            throw new UnsupportedOperationException("Just kidding, can't get here!");
        }
    }

    public void initAction(String actionName) {
        action = new ControllerAction(ControllerActionTestController::new, actionName);
    }

    @Test
    public void shouldCallTheMethodPassedIn() {
        initAction("setCalled");
        GET("/theRequestedPath", this::handleRequest);
        assertCalled();
    }

    @Test
    public void shouldDoSomethingAboutPrivateMethods() {
        initAction("tryPrivateAction");
        GET("/theRequestedPath", this::handleRequest);
        assertNotFound();
    }

    private void handleRequest(ServletRequest servletRequest, ServletResponse servletResponse) {
        this.servletRequest = servletRequest;
        this.servletResponse = servletResponse;
        try {
            action.handleRequest(servletRequest, servletResponse);
        } catch (ServletException e) {
            this.servletException = e;
        }
    }

    private void assertCalled() {
        assertTrue("Expected action to have been called", getViewContext().getBoolean("CALLED"));
    }

    private void assertNotFound() {
        assertEquals(404, ((HttpServletResponse) servletResponse).getStatus());
    }

    private ViewContext getViewContext() {
        return (ViewContext) servletRequest.getAttribute(ControllerAction.VIEW_CONTEXT);
    }
}
