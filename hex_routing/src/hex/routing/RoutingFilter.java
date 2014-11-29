/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Jason E. Wall
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package hex.routing;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

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
            routingConfig.getRouteHandler(request.getMethod(), getPath(servletRequest))
                    .handleRequest(servletRequest, servletResponse);
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
