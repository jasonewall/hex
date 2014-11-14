package hex.routing;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jason on 14-11-11.
 */
public class RoutingFilter implements Filter {

    public static final String CONTEXT_ATTRIBUTE = "hex.routing.RoutingFilter.CONTEXT_ATTRIBUTE";

    private RoutingConfig routingConfig;

    private String configAttributeName = Routing.CONFIG;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        if(filterConfig.getInitParameter(CONTEXT_ATTRIBUTE) != null) {
            this.configAttributeName = filterConfig.getInitParameter(CONTEXT_ATTRIBUTE);
        }
        this.routingConfig = (RoutingConfig) filterConfig.getServletContext().getAttribute(configAttributeName);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        if(routingConfig.hasRoute(request.getMethod(), getPath(servletRequest))) {
            routingConfig.getRouteHandler(getPath(servletRequest)).handleRequest(servletRequest, servletResponse);
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }

    public void setRoutingConfig(RoutingConfig config) {
        routingConfig = config;
    }

    protected String getPath(ServletRequest servletRequest) {
        HttpServletRequest request = ((HttpServletRequest)servletRequest);
        return request.getRequestURI().replaceFirst(request.getContextPath(), "");
    }
}
