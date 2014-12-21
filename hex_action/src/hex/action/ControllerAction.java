package hex.action;

import hex.action.params.Param;
import hex.action.params.RouteParam;
import hex.action.initialization.InitializationException;
import hex.action.params.WebParams;
import hex.routing.Route;
import hex.routing.RouteHandler;
import hex.routing.RouteParams;
import hex.utils.coercion.CoercionException;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Predicate;
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

    private Properties hexActionConfig = new Properties();

    private Optional<Method> action;

    public void setHexActionConfig(Properties hexActionConfig) {
        this.hexActionConfig = hexActionConfig;
    }

    public ControllerAction(Supplier<Controller> supplier, String actionName) {
        this.supplier = supplier;
        this.actionName = actionName;
    }

    @Override
    public void handleRequest(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        checkException(servletRequest);
        Controller controller = supplier.get();
        prepareController(controller, servletRequest, servletResponse);
        try {
            if(getAction().isPresent()) {
                servletRequest.setAttribute(VIEW_CONTEXT, controller.view);
                invokeAction(action.get(), controller, (RouteParams) servletRequest.getAttribute(Route.ROUTE_PARAMS));
                if(!servletResponse.isCommitted()) {
                    controller.renderAction(actionName);
                }
            } else {
                renderActionNotFound(servletResponse, String.format("Action method (%s) not found in controller (%s)", actionName, controller.getClass().getSimpleName()));
            }
        } catch (InvocationTargetException | CoercionException e) {
            throw new ServletException(e.getCause());
        } catch (IllegalAccessException | IllegalArgumentException e) {
            renderActionNotFound(servletResponse, e.getMessage());
        } catch (ActionAbortedException e) {
            if(e.getServletExceptionCause().isPresent()) {
                throw e.getServletExceptionCause().get();
            } else {
                throw e.getIOExceptionCause().get();
            }
        }
    }

    public void prepareController(Controller controller, ServletRequest servletRequest, ServletResponse servletResponse) {
        controller.request = (HttpServletRequest)servletRequest;
        controller.response = (HttpServletResponse)servletResponse;
        controller.params = new WebParams(servletRequest, (RouteParams)servletRequest.getAttribute(Route.ROUTE_PARAMS));
        if(hexActionConfig.containsKey("viewBase")) {
            controller.setViewBase(hexActionConfig.getProperty("viewBase"));
        }
    }

    private void checkException(ServletRequest request) throws ServletException {
        InitializationException e = (InitializationException)
                request.getServletContext().getAttribute(InitializationException.class.getName());
        if(e == null) return;

        throw new ServletException(Errors.INITIALIZATION_ERROR_OCCURRED, e);
    }

    private void invokeAction(Method method, Controller controller, RouteParams routeParams) throws InvocationTargetException, IllegalAccessException, CoercionException {
        Predicate<Parameter> missingParamAnnotations = p -> !p.isAnnotationPresent(RouteParam.class) && !p.isAnnotationPresent(Param.class);
        if(Stream.of(method.getParameters()).anyMatch(missingParamAnnotations)) {
            throw new IllegalAccessException(String.format("Missing route parameter annotation in %s#%s for arguments: %s",
                    controller.getClass().getSimpleName(),
                    actionName,
                    Stream.of(method.getParameters()).filter(missingParamAnnotations).map(Parameter::getName)
                            .collect(Collectors.joining(", "))
            ));
        }
        List<Object> paramValues = new ArrayList<>();
        for(Parameter param : method.getParameters()) {
            if(param.isAnnotationPresent(RouteParam.class)) {
                paramValues.add(routeParams.get(param.getType(), param.getAnnotation(RouteParam.class).value()));
            } else {
                paramValues.add(controller.params.get(param.getType(), param.getAnnotation(Param.class).value()));
            }
        }
        method.invoke(controller, paramValues.toArray());
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
