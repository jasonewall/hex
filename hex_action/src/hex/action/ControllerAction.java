package hex.action;

import hex.action.annotations.RouteParam;
import hex.routing.Route;
import hex.routing.RouteHandler;
import hex.routing.RouteParams;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by jason on 14-11-15.
 */
public class ControllerAction implements RouteHandler {
    public static final String VIEW_CONTEXT = "hex.action.ControllerAction.VIEW_CONTEXT";

    private final String actionName;

    private final Supplier<Controller> supplier;

    private Optional<Method> action;

    public ControllerAction(Supplier<Controller> supplier, String actionName) {
        this.supplier = supplier;
        this.actionName = actionName;
    }

    @Override
    public void handleRequest(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        Controller controller = supplier.get();
        prepareController(controller, servletRequest, servletResponse);
        try {
            if(getAction().isPresent()) {
                invokeAction(action.get(), controller, (RouteParams) servletRequest.getAttribute(Route.ROUTE_PARAMS));
            } else {
                renderActionNotFound(servletResponse, String.format("Action method (%s) not found in Controller(%s)", actionName, controller.getClass().getSimpleName()));
            }
        } catch (InvocationTargetException e) {
            throw new ServletException(e.getCause());
        } catch (IllegalAccessException | IllegalArgumentException e) {
            renderActionNotFound(servletResponse, e.getMessage());
        }
    }

    public void prepareController(Controller controller, ServletRequest servletRequest, ServletResponse servletResponse) {
        controller.request = (HttpServletRequest)servletRequest;
        controller.response = (HttpServletResponse)servletResponse;
    }

    private void invokeAction(Method method, Controller controller, RouteParams routeParams) throws InvocationTargetException, IllegalAccessException {
        if(Stream.of(method.getParameters()).anyMatch(p -> !p.isAnnotationPresent(RouteParam.class))) {
            throw new IllegalAccessException(String.format("Missing route parameter annotation in %s#%s for arguments: %s",
                    controller.getClass().getSimpleName(),
                    actionName,
                    Stream.of(method.getParameters()).filter(p -> !p.isAnnotationPresent(RouteParam.class)).map(Parameter::getName)
                    .collect(Collectors.joining(", "))
                    ));
        }
        Object[] params = Stream.of(method.getParameters())
                .map(p -> routeParams.get(p.getType(), p.getAnnotation(RouteParam.class).value()))
                .toArray();
        method.invoke(controller, params);
    }

    private void renderActionNotFound(ServletResponse servletResponse, String message) throws IOException {
        ((HttpServletResponse) servletResponse).sendError(404, message);
    }

    private Optional<Method> getAction() {
        if(action == null) {
            Controller instance = supplier.get();
            Class<? extends Controller> klass = instance.getClass();
            action = Stream.of(klass.getMethods())
                    .filter(m -> actionName.equals(m.getName()))
                    .findFirst()
                    ;
        }

        return action;
    }
}
