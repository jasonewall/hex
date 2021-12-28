package hex.action;

import hex.action.routing.RouteManager;
import hex.action.initialization.InitializationException;
import hex.action.initialization.InitializerRunner;
import hex.routing.Routing;
import hex.routing.RoutingConfig;
import hex.routing.RoutingConfigBase;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Properties;
import java.util.stream.Stream;

/**
 * Class for representing a hex application. Implements {@link javax.servlet.ServletContextListener} so it can be used
 * for deploying a hex application in web.xml. This should not be used directly in development environments if you want
 * to maintain on-demand class loading. You should use {@code hex.dev.DevRoutingFilter} in the {@code hex_dev} module
 * instead. (See {@code DevRoutingFilter} docs for more info.)
 */
public class Application implements ServletContextListener {
    public static final String CONFIG = "hex.properties";
    public static final String ACTION_CONFIG = "hex_action.properties";
    public static final String ROUTES = "routes";
    public static final String ROOT = "hex.action.Application.ROOT";
    public static final String ROUTING_CONFIG_CLASSES = "hex.action.Application.ROUTING_CONFIG_CLASSES";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            initializeApplication(sce.getServletContext());
        } catch (InitializationException e) {
            sce.getServletContext().setAttribute(InitializationException.class.getName(), e);
        }
    }

    private void initializeApplication(ServletContext application) throws InitializationException {
        ClassLoader cl = getClass().getClassLoader();
        try {
            RoutingConfig routingConfig = initializeRoutes(cl, application);
            application.setAttribute(Routing.CONFIG, routingConfig);

            InitializerRunner initializer = new InitializerRunner();
            initializer.run();
        } catch (ServletException e) {
            throw new InitializationException(e.getCause());
        }
    }

    /**
     * Receives notification that the ServletContext is about to be
     * shut down.
     * <p>
     * <p>All servlets and filters will have been destroyed before any
     * ServletContextListeners are notified of context
     * destruction.
     *
     * @param sce the ServletContextEvent containing the ServletContext
     *            that is being destroyed
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }

    public static RoutingConfig initializeRoutes(ClassLoader classLoader, ServletContext application) throws ServletException {
        try {
            Properties adapterProperties = new Properties();
            adapterProperties.load(classLoader.getResourceAsStream(ACTION_CONFIG));

            String[] routingConfigClasses = Stream.of(adapterProperties.getProperty(ROUTES).split(","))
                    .map(String::trim).toArray(String[]::new);

            application.setAttribute(ROUTING_CONFIG_CLASSES, routingConfigClasses);

            RoutingConfigBase routingConfig = new RoutingConfigBase();
            for (String className : routingConfigClasses) {
                RouteManager routeManager = (RouteManager) Class.forName(className, false, classLoader).newInstance();
                routeManager.setHexActionProperties(adapterProperties);
                routeManager.defineRoutes();
                application.setAttribute(className, routeManager);
                //TODO: This is smelly. Need to decide if supporting multiple RouteManagers in the same routing config is a good idea or not.
                routeManager.getDefinedRoutes().forEach(routingConfig::addRoute);
            }
            return routingConfig;
        } catch (InstantiationException | ClassNotFoundException | IllegalAccessException | IOException e) {
            throw new ServletException(e);
        }
    }
}
