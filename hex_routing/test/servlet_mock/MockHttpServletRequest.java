package servlet_mock;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.*;

/**
 * Created by jason on 14-11-16.
 */
public class MockHttpServletRequest implements HttpServletRequest {
    private Map<String,Object> attributes = new HashMap<>();
    @Override
    public Object getAttribute(String attributeName) {
        return attributes.get(attributeName);
    }

    private static class IterableEnumeration<T> implements Enumeration<T> {
        private Iterator<T> iterator;

        public IterableEnumeration(Iterable<T> iterable) {
            iterator = iterable.iterator();
        }
        @Override
        public boolean hasMoreElements() {
            return iterator.hasNext();
        }

        @Override
        public T nextElement() {
            return iterator.next();
        }
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return new IterableEnumeration<>(attributes.keySet());
    }

    @Override
    public String getCharacterEncoding() {
        return null;
    }

    @Override
    public void setCharacterEncoding(String s) throws UnsupportedEncodingException {

    }

    @Override
    public int getContentLength() {
        return 0;
    }

    @Override
    public long getContentLengthLong() {
        return 0;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return null;
    }

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private Map<String,List<String>> parameters = new HashMap<>();

    @Override
    public String getParameter(String name) {
        if(!parameters.containsKey(name)) return null;
        return parameters.get(name).stream().findFirst().orElse(null);
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return new IterableEnumeration<>(parameters.keySet());
    }

    @Override
    public String[] getParameterValues(String name) {
        if(!parameters.containsKey(name)) return null;
        return parameters.get(name).stream().toArray(String[]::new);
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String,String[]> results = new HashMap<>();
        parameters.forEach((k,v) -> results.put(k, v.stream().toArray(String[]::new)));
        return results;
    }

    public void putParam(String name, String value) {
        parameters.merge(name, new ArrayList<>(Arrays.asList(value)), (o,n) -> {
            o.addAll(n);
            return o;
        });
    }

    @Override
    public String getProtocol() {
        return null;
    }

    @Override
    public String getScheme() {
        return null;
    }

    @Override
    public String getServerName() {
        return null;
    }

    @Override
    public int getServerPort() {
        return 0;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return null;
    }

    @Override
    public String getRemoteAddr() {
        return null;
    }

    @Override
    public String getRemoteHost() {
        return null;
    }

    @Override
    public void setAttribute(String attribute, Object value) {
        attributes.put(attribute, value);
    }

    @Override
    public void removeAttribute(String attribute) {
        attributes.remove(attribute);
    }

    @Override
    public Locale getLocale() {
        return null;
    }

    @Override
    public Enumeration<Locale> getLocales() {
        return null;
    }

    @Override
    public boolean isSecure() {
        return false;
    }

    private String renderedPage;

    public String getRenderedPage() {
        return renderedPage;
    }

    private List<String> includedPages = new ArrayList<>();

    public List<String> getIncludedPages() {
        return includedPages;
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String path) {
        return new RequestDispatcher() {
            @Override
            public void forward(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
                renderedPage = path;
                servletResponse.setContentType("text/html");
            }

            @Override
            public void include(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
                includedPages.add(path);
                servletResponse.setContentType("text/html");
            }
        };
    }

    @Override @Deprecated
    public String getRealPath(String s) {
        return null;
    }

    @Override
    public int getRemotePort() {
        return 0;
    }

    @Override
    public String getLocalName() {
        return null;
    }

    @Override
    public String getLocalAddr() {
        return null;
    }

    @Override
    public int getLocalPort() {
        return 0;
    }

    @Override
    public ServletContext getServletContext() {
        return MockServletContext.NULL;
    }

    @Override
    public AsyncContext startAsync() throws IllegalStateException {
        return null;
    }

    @Override
    public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {
        return null;
    }

    @Override
    public boolean isAsyncStarted() {
        return false;
    }

    @Override
    public boolean isAsyncSupported() {
        return false;
    }

    @Override
    public AsyncContext getAsyncContext() {
        return null;
    }

    @Override
    public DispatcherType getDispatcherType() {
        return null;
    }

    @Override
    public String getAuthType() {
        return null;
    }

    @Override
    public Cookie[] getCookies() {
        return new Cookie[0];
    }

    @Override
    public long getDateHeader(String s) {
        return 0;
    }

    @Override
    public String getHeader(String s) {
        return null;
    }

    @Override
    public Enumeration<String> getHeaders(String s) {
        return null;
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        return null;
    }

    @Override
    public int getIntHeader(String s) {
        return 0;
    }

    @Override
    public String getMethod() {
        return null;
    }

    @Override
    public String getPathInfo() {
        return null;
    }

    @Override
    public String getPathTranslated() {
        return null;
    }

    @Override
    public String getContextPath() {
        return null;
    }

    @Override
    public String getQueryString() {
        return null;
    }

    @Override
    public String getRemoteUser() {
        return null;
    }

    @Override
    public boolean isUserInRole(String s) {
        return false;
    }

    @Override
    public Principal getUserPrincipal() {
        return null;
    }

    @Override
    public String getRequestedSessionId() {
        return null;
    }

    @Override
    public String getRequestURI() {
        return null;
    }

    @Override
    public StringBuffer getRequestURL() {
        return null;
    }

    @Override
    public String getServletPath() {
        return null;
    }

    @Override
    public HttpSession getSession(boolean b) {
        return null;
    }

    @Override
    public HttpSession getSession() {
        return null;
    }

    @Override
    public String changeSessionId() {
        return null;
    }

    @Override
    public boolean isRequestedSessionIdValid() {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromCookie() {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromURL() {
        return false;
    }

    @Override @Deprecated
    public boolean isRequestedSessionIdFromUrl() {
        return false;
    }

    @Override
    public boolean authenticate(HttpServletResponse httpServletResponse) throws IOException, ServletException {
        return false;
    }

    @Override
    public void login(String s, String s2) throws ServletException {

    }

    @Override
    public void logout() throws ServletException {

    }

    @Override
    public Collection<Part> getParts() throws IOException, ServletException {
        return null;
    }

    @Override
    public Part getPart(String s) throws IOException, ServletException {
        return null;
    }

    @Override
    public <T extends HttpUpgradeHandler> T upgrade(Class<T> tClass) throws IOException, ServletException {
        return null;
    }
}
