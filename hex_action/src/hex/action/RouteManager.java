package hex.action;

import hex.routing.HttpMethod;
import hex.routing.Route;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Created by jason on 14-11-14.
 */
public class RouteManager {
    private List<Route> definedRoutes = new ArrayList<>();

    public List<Route> getDefinedRoutes() {
        return definedRoutes;
    }

    public void defineRoutes() {

    }

    public void get(String path, Supplier<Controller> controllerSupplier, String action) {
        definedRoutes.add(new Route(HttpMethod.GET, path, new ControllerAction(controllerSupplier, action)));
    }

    public void post(String path, Supplier<Controller> controllerSupplier, String action) {
        definedRoutes.add(new Route(HttpMethod.POST, path, new ControllerAction(controllerSupplier, action)));
    }

    public void put(String path, Supplier<Controller> controllerSupplier, String action) {
        definedRoutes.add(new Route(HttpMethod.PUT, path, new ControllerAction(controllerSupplier, action)));
    }

    public void delete(String path, Supplier<Controller> controllerSupplier, String action) {
        definedRoutes.add(new Route(HttpMethod.DELETE, path, new ControllerAction(controllerSupplier, action)));
    }

    public void matches(String path, Supplier<Controller> controllerSupplier, String action) {
        definedRoutes.add(new Route(HttpMethod.ANY, path, new ControllerAction(controllerSupplier, action)));
    }
}
