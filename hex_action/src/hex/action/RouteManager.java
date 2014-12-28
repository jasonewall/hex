package hex.action;

import hex.routing.HttpMethod;
import hex.routing.Route;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.function.Supplier;

/**
 * Created by jason on 14-11-14.
 */
public class RouteManager {
    private List<Route> definedRoutes = new ArrayList<>();

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

    public void get(String path, Supplier<Controller> controllerSupplier, String action) {
        addRoute(new Route(HttpMethod.GET, path, getHandler(controllerSupplier, action)));
    }

    public void post(String path, Supplier<Controller> controllerSupplier, String action) {
        addRoute(new Route(HttpMethod.POST, path, getHandler(controllerSupplier, action)));
    }

    public void put(String path, Supplier<Controller> controllerSupplier, String action) {
        addRoute(new Route(HttpMethod.PUT, path, getHandler(controllerSupplier, action)));
    }

    public void delete(String path, Supplier<Controller> controllerSupplier, String action) {
        addRoute(new Route(HttpMethod.DELETE, path, getHandler(controllerSupplier, action)));
    }

    public void matches(String path, Supplier<Controller> controllerSupplier, String action) {
        addRoute(new Route(HttpMethod.ANY, path, getHandler(controllerSupplier, action)));
    }

    private void addRoute(Route route) {
        definedRoutes.add(route);
    }

    private ControllerAction getHandler(Supplier<Controller> controllerSupplier, String action) {
        ControllerAction controllerAction = new ControllerAction(controllerSupplier, action);
        controllerAction.setHexActionConfig(getHexActionConfig());
        return controllerAction;
    }
}
