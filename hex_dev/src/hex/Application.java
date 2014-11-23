package hex;

import hex.action.RouteManager;
import hex.routing.RoutingConfig;
import hex.routing.RoutingConfigBase;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Properties;
import java.util.stream.Stream;

/**
 * Created by jason on 14-11-22.
 */
public class Application {
    public static final String CONFIG = "hex.properties";
    public static final String ACTION_CONFIG = "hex_action.properties";
    public static final String ROUTES = "routes";
    public static final String ROOT = "hex.Application.ROOT";

    public static RoutingConfig initializeRoutes(ClassLoader classLoader) throws ServletException {
        try {
            Properties adapterProperties = new Properties();
            adapterProperties.load(classLoader.getResourceAsStream(ACTION_CONFIG));

            String[] routingConfigClasses = Stream.of(adapterProperties.getProperty(ROUTES).split(","))
                    .map(String::trim).toArray(String[]::new);

            RoutingConfigBase routingConfig = new RoutingConfigBase();
            for (String className : routingConfigClasses) {
                RouteManager routeManager = (RouteManager) Class.forName(className, false, classLoader).newInstance();
                routeManager.getDefinedRoutes().forEach(routingConfig::addRoute);
            }
            return routingConfig;
        } catch (InstantiationException | ClassNotFoundException | IllegalAccessException | IOException e) {
            throw new ServletException(e);
        }
    }
}
