package hex.routing;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * Created by jason on 14-11-11.
 */
@FunctionalInterface
public interface RouteHandler {
    public void handleRequest(ServletRequest servletRequest, ServletResponse servletResponse);
}
