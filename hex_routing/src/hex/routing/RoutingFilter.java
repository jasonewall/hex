/*
 * Copyright 2014 Jason E. Wall
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
