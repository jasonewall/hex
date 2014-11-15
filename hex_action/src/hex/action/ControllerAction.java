package hex.action;

import hex.routing.RouteHandler;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.function.Supplier;

/**
 * Created by jason on 14-11-15.
 */
public class ControllerAction implements RouteHandler {
    private final String actionName;

    private final Supplier<Controller> supplier;

    public ControllerAction(Supplier<Controller> supplier, String actionName) {
        this.supplier = supplier;
        this.actionName = actionName;
    }
    @Override
    public void handleRequest(ServletRequest servletRequest, ServletResponse servletResponse) {

    }
}
