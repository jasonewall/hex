package hex.action.routing;

import hex.action.Controller;
import hex.action.ControllerAction;
import hex.routing.HttpMethod;
import hex.routing.Route;

import java.util.*;
import java.util.function.Supplier;

/**
 * Created by jason on 14-11-14.
 */
public class RouteManager {
    private final Map<String,Route> routeMap = new HashMap<>();

    private final List<Route> definedRoutes = new ArrayList<>();

    private Properties hexActionProperties;

    public Properties getHexActionConfig() {
        return hexActionProperties;
    }

    public void setHexActionProperties(Properties hexActionProperties) {
        this.hexActionProperties = hexActionProperties;
    }

    public List<Route> getDefinedRoutes() {
        return definedRoutes;
    }

    public RouteManager() {
    }

    public void defineRoutes() {

    }

    protected void get(String path, Supplier<Controller> controllerSupplier, String action) {
        addRoute(HttpMethod.GET, path, getHandler(controllerSupplier, action));
    }

    protected void post(String path, Supplier<Controller> controllerSupplier, String action) {
        addRoute(HttpMethod.POST, path, getHandler(controllerSupplier, action));
    }

    protected void put(String path, Supplier<Controller> controllerSupplier, String action) {
        addRoute(HttpMethod.PUT, path, getHandler(controllerSupplier, action));
    }

    protected void delete(String path, Supplier<Controller> controllerSupplier, String action) {
        addRoute(HttpMethod.DELETE, path, getHandler(controllerSupplier, action));
    }

    protected void matches(String path, Supplier<Controller> controllerSupplier, String action) {
        definedRoutes.add(new Route(HttpMethod.ANY, path, getHandler(controllerSupplier, action)));
    }

    private void addRoute(HttpMethod method, String path, ControllerAction handler) {
        Route route = new Route(method, path, handler);
        String pathName = Route.PATH_PARAM_PATTERN.matcher(path).replaceAll("")
                .replaceFirst("/", "")
                .replaceAll("/", "_");
        definedRoutes.add(route);
        routeMap.put(String.format("%s_%s", handler.getName(), pathName), route);
    }

    Route getRouteNamed(String routeName) {
        return routeMap.get(routeName);
    }

    private ControllerAction getHandler(Supplier<Controller> controllerSupplier, String action) {
        ControllerAction controllerAction = new ControllerAction(controllerSupplier, action);
        controllerAction.setHexActionConfig(getHexActionConfig());
        return controllerAction;
    }
}
