package hex.action;

import hex.routing.RouteHandler;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.function.Supplier;
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
    public void handleRequest(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException {
        Controller controller = supplier.get();
        controller.viewContext = new ViewContext();
        servletRequest.setAttribute(VIEW_CONTEXT, controller.viewContext);
        try {
            if(getAction().isPresent()) {
                action.get().invoke(controller);
            } else {
                renderActionNotFound(servletResponse);
            }
        } catch (InvocationTargetException e) {
            e.getCause().printStackTrace();
        } catch (IllegalAccessException e) {
            renderActionNotFound(servletResponse);
        }

    }

    private void renderActionNotFound(ServletResponse servletResponse) {
        ((HttpServletResponse) servletResponse).setStatus(404);
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
