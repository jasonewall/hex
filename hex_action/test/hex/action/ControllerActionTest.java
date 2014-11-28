package hex.action;

import hex.action.annotations.RouteParam;
import hex.routing.Route;
import hex.routing.RouteParams;
import org.junit.After;
import org.junit.Test;
import servlet_mock.MockHttpServletRequest;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

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

    private RouteParams routeParams;

    @SuppressWarnings("UnusedDeclaration")
    class ControllerActionTestsController extends Controller {
        public void setCalled() {
            view.put("CALLED", true);
        }

        public void errorInAction() {
            throw new RuntimeException("What happens when our action goes bad?");
        }

        private void tryPrivateAction() {
            throw new UnsupportedOperationException("Just kidding, can't get here!");
        }

        public void withRouteParams(@RouteParam("id") int id) {
            view.put("id", id);
        }

        public void misMatchedParamTypes(@RouteParam("id") int id) {
            fail("Expected the invocation to fail because if type mismatches.");
        }

        public void missingRouteParamAnnotation(String username) {
            fail("Expected the invocation to fail because we don't know what route param to use.");
        }
    }

    @After
    public void assertSuccess() {
        assertNull(servletException);
    }

    private void initAction(String actionName) {
        action = new ControllerAction(ControllerActionTestsController::new, actionName);
    }

    private void initRouteParams(Consumer<Map<String,String>> paramsConsumer) {
        Map<String,String> params = new HashMap<>();
        paramsConsumer.accept(params);
        routeParams = new RouteParams(params);
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

    @Test
    public void shouldPropegateActionExceptionsWithAServletException() {
        initAction("errorInAction");
        GET("/theRequestPath", this::handleRequest);
        assert500();
    }

    @Test
    public void shouldHandleRouteParamsInMethodParams() {
        initAction("withRouteParams");
        initRouteParams(p -> p.put("id", "8"));
        GET("/theRequestPath", this::handleRequest)
                .andThen((q,r) -> assertEquals(8, getViewContext().getInt("id")))
                ;
    }

    @Test
    public void shouldBeSmartAboutTypeMismatchesInRouteParams() {
        initAction("misMatchedParamTypes");
        initRouteParams(p -> p.put("id", "neil.dt"));
        GET("/profile/neil.dt", this::handleRequest);
        assertNotFound();
    }

    @Test
    public void shouldBeNotFoundWhenParamAnnoationMissing() {
        initAction("missingRouteParamAnnotation");
        initRouteParams(p -> p.put("username", "a.einstein"));
        GET("/profile/a.einstein", this::handleRequest);
        assertNotFound();
    }

    @Test
    public void shouldRenderDefaultRoutes() {
        initAction("withRouteParams");
        initRouteParams(p -> p.put("id", "17"));
        GET("/controller_action_tests/17", this::handleRequest);
        assertRendered("/controller_action_tests/with_route_params.html.jsp");
    }

    @Test
    public void shouldBeSmartAboutGoingToIndexRoutes() {
        initAction("setCalled");
        GET("/does_this_really_matter", this::handleRequest);
        assertRendered("/controller_action_tests/set_called.html.jsp");
    }

    private void handleRequest(ServletRequest servletRequest, ServletResponse servletResponse) {
        this.servletRequest = servletRequest;
        this.servletResponse = servletResponse;
        Optional.ofNullable(routeParams).ifPresent(p -> servletRequest.setAttribute(Route.ROUTE_PARAMS, p));
        try {
            action.handleRequest(servletRequest, servletResponse);
        } catch (ServletException e) {
            this.servletException = e;
        } catch (IOException e) {
            e.printStackTrace(); // basically shouldn't happen in test world
        }
    }

    private void assertCalled() {
        assertTrue("Expected action to have been called", getViewContext().getBoolean("CALLED"));
    }

    private void assertNotFound() {
        assertEquals(404, ((HttpServletResponse) servletResponse).getStatus());
    }

    private void assert500() {
        assertNotNull(servletException);
        servletException = null;
    }

    private void assertRendered(String jspFilePath) {
        MockHttpServletRequest request = (MockHttpServletRequest) servletRequest;
        assertEquals(jspFilePath, request.getRenderedPage());
    }

    private ViewContext getViewContext() {
        return (ViewContext) servletRequest.getAttribute(ControllerAction.VIEW_CONTEXT);
    }
}
