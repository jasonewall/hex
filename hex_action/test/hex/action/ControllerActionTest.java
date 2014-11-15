package hex.action;

import org.junit.After;
import org.junit.Test;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import static servlet_mock.HttpMock.*;
import static org.junit.Assert.*;

/**
 * Created by jason on 14-11-15.
 */
public class ControllerActionTest {
    private ControllerAction action;

    private ServletRequest servletRequest;

    class ControllerActionTestController extends Controller {
        public void setCalled() {
            viewContext.put("CALLED", true);
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

    private void handleRequest(ServletRequest servletRequest, ServletResponse servletResponse) {
        this.servletRequest = servletRequest;
        action.handleRequest(servletRequest, servletResponse);
    }

    private void assertCalled() {
        assertTrue("Expected action to have been called", getViewContext().getBoolean("CALLED"));
    }

    private ViewContext getViewContext() {
        return (ViewContext) servletRequest.getAttribute(ControllerAction.VIEW_CONTEXT);
    }
}
