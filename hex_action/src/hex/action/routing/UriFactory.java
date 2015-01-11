package hex.action.routing;

import hex.action.Application;
import hex.routing.Route;

import javax.servlet.ServletRequest;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jason on 15-01-03.
 */
public class UriFactory {
    private static final Pattern PATH_NAME_PATTERN = Pattern.compile("(.*)_(url|path)$");

    private static final String URL = "url";

    public static Uri getUrlFor(String pathRef, ServletRequest request) {
        return getUrlFor(pathRef, request, null);
    }

    public static Uri getUrlFor(String pathRef, ServletRequest request, Object values) {
        Matcher m = PATH_NAME_PATTERN.matcher(pathRef);
        if(!m.matches()) throw new IllegalArgumentException("Path reference did not match format (path_name)_(url|path)");
        String pathName = m.group(1);
        String uriType = m.group(2);
        Route route = getRouteNamed(pathName, request);
        Uri uri = getUri(uriType, request);
        uri.setContext(request.getServletContext().getContextPath());
        // TODO: add check for Tomcat mode with it's stupid trailing slash in "directory paths"
        uri.setPath(route.createPath(values));
        return uri;
    }

    private static Uri getUri(String uriType, ServletRequest request) {
        if(URL.equals(uriType)) {
            return Uri.create(request.getScheme(), request.getServerName(), request.getServerPort());
        }
        return new Uri();
    }

    private static Route getRouteNamed(String pathName, ServletRequest request) {
        String[] routingConfigClasses = (String[]) request.getServletContext().getAttribute(Application.ROUTING_CONFIG_CLASSES);
        for(String configClass : routingConfigClasses) {
            RouteManager routingConfig = (RouteManager) request.getServletContext().getAttribute(configClass);
            Route route = routingConfig.getRouteNamed(pathName);
            if(route != null) return route;
        }
        throw new NoSuchElementException(String.format(Errors.ROUTE_NAME_NOT_FOUND, pathName));
    }
}
