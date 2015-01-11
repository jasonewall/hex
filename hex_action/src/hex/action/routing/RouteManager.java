package hex.action.routing;

import hex.action.Controller;
import hex.action.ControllerAction;
import hex.routing.HttpMethod;
import hex.routing.Route;

import java.util.*;
import java.util.function.Predicate;
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
        addRoute(HttpMethod.ANY, path, getHandler(controllerSupplier, action));
    }

    private void addRoute(Predicate<HttpMethod> any, String path, ControllerAction handler) {
        Route route = new Route(any, path, handler);
        definedRoutes.add(route);
        registerRouteName(handler.getName(), path, route);
    }

    private void addRoute(HttpMethod method, String path, ControllerAction handler) {
        Route route = new Route(method, path, handler);
        definedRoutes.add(route);
        registerRouteName(handler.getName(), path, route);
    }

    private void registerRouteName(String actionName, String path, Route route) {
        String pathName = Route.PATH_PARAM_PATTERN.matcher(path).replaceAll("")
                .replaceFirst("/", "")
                .replaceAll("/", "_");
        String routeName = actionName;
        if(pathName.length() > 0) routeName += "_" + pathName;
        routeMap.put(routeName, route);
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
