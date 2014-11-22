package hex.dev;

import hex.action.RouteManager;
import hex.routing.*;

import javax.servlet.*;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by jason on 14-11-21.
 */
public class DevRoutingFilter implements Filter, RoutingConfig {
    private RoutingConfigBase config;

    private RoutingFilter filter = new RoutingFilter();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        filterConfig.getServletContext().setAttribute(Routing.CONFIG, this);
        filter.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try (URLClassLoader requestClassLoader = new URLClassLoader(new URL[]{new URL("file:/Users/jason/Code/embedded_jetty/out/production/")})) {
            Compiler.compile();
            config = new RoutingConfigBase();
            RouteManager routeManager = (RouteManager) requestClassLoader.loadClass("embedded_jetty.ApplicationRoutes").newInstance();
            routeManager.getDefinedRoutes().forEach(config::addRoute);
            filter.doFilter(servletRequest, servletResponse, filterChain);
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            throw new ServletException(e);
        } finally {
            config = null;
        }
    }

    @Override
    public void destroy() {
        filter.destroy();
    }

    @Override
    public boolean hasRoute(String path) {
        return config.hasRoute(path);
    }

    @Override
    public boolean hasRoute(String method, String path) {
        return config.hasRoute(method, path);
    }

    @Override
    public boolean hasRoute(HttpMethod method, String path) {
        return config.hasRoute(method, path);
    }

    @Override
    public RouteHandler getRouteHandler(String path) {
        return config.getRouteHandler(path);
    }

    @Override
    public RouteHandler getRouteHandler(String method, String path) {
        return config.getRouteHandler(method, path);
    }

    @Override
    public RouteHandler getRouteHandler(HttpMethod method, String path) {
        return config.getRouteHandler(method, path);
    }
}
