package servlet_mock.jsp;

import servlet_mock.MockHttpServletRequest;

import javax.el.ELContext;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.el.ExpressionEvaluator;
import javax.servlet.jsp.el.VariableResolver;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * Created by jason on 14-12-23.
 */
public class MockJspContext extends PageContext {
    private final Map<String,Object> attributes = new HashMap<>();

    private final HttpServletRequest request;

    private MockJspWriter out;

    public MockJspContext(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * Register the name and value specified with page scope semantics.
     * If the value passed in is <code>null</code>, this has the same
     * effect as calling
     * <code>removeAttribute( name, PageContext.PAGE_SCOPE )</code>.
     *
     * @param name  the name of the attribute to set
     * @param value the value to associate with the name, or null if the
     *              attribute is to be removed from the page scope.
     * @throws NullPointerException if the name is null
     */
    @Override
    public void setAttribute(String name, Object value) {
        Objects.requireNonNull(name);
        if(value == null) {
            attributes.remove(name);
            return;
        }
        attributes.put(name, value);
    }

    /**
     * Register the name and value specified with appropriate
     * scope semantics.  If the value passed in is <code>null</code>,
     * this has the same effect as calling
     * <code>removeAttribute( name, scope )</code>.
     *
     * @param name  the name of the attribute to set
     * @param value the object to associate with the name, or null if
     *              the attribute is to be removed from the specified scope.
     * @param scope the scope with which to associate the name/object
     * @throws NullPointerException     if the name is null
     * @throws IllegalArgumentException if the scope is invalid
     * @throws IllegalStateException    if the scope is
     *                                  PageContext.SESSION_SCOPE but the page that was requested
     *                                  does not participate in a session or the session has been
     *                                  invalidated.
     */
    @Override
    public void setAttribute(String name, Object value, int scope) {
        Objects.requireNonNull(name);
        switch(scope) {
            case PageContext.PAGE_SCOPE:
                setAttribute(name, value);
                break;
            case PageContext.REQUEST_SCOPE:
                request.setAttribute(name, value);
                break;
            case PageContext.SESSION_SCOPE:
                request.getSession().setAttribute(name, value);
                break;
            case PageContext.APPLICATION_SCOPE:
                request.getServletContext().setAttribute(name, value);
                break;
            default:
                throw new IllegalArgumentException(String.format("Scope %d is invalid.", scope));
        }
    }

    /**
     * Returns the object associated with the name in the page scope or null
     * if not found.
     *
     * @param name the name of the attribute to get
     * @return the object associated with the name in the page scope
     * or null if not found.
     * @throws NullPointerException if the name is null
     */
    @Override
    public Object getAttribute(String name) {
        Objects.requireNonNull(name);
        return attributes.get(name);
    }

    /**
     * Return the object associated with the name in the specified
     * scope or null if not found.
     *
     * @param name  the name of the attribute to set
     * @param scope the scope with which to associate the name/object
     * @return the object associated with the name in the specified
     * scope or null if not found.
     * @throws NullPointerException     if the name is null
     * @throws IllegalArgumentException if the scope is invalid
     * @throws IllegalStateException    if the scope is
     *                                  PageContext.SESSION_SCOPE but the page that was requested
     *                                  does not participate in a session or the session has been
     *                                  invalidated.
     */
    @Override
    public Object getAttribute(String name, int scope) {
        Objects.requireNonNull(name);
        switch(scope) {
            case PageContext.PAGE_SCOPE:
                return getAttribute(name);
            case PageContext.REQUEST_SCOPE:
                return request.getAttribute(name);
            case PageContext.SESSION_SCOPE:
                return request.getSession().getAttribute(name);
            case PageContext.APPLICATION_SCOPE:
                return request.getServletContext().getAttribute(name);
            default:
                throw new IllegalArgumentException(String.format("Scope %d is invalid.", scope));
        }
    }

    /**
     * Searches for the named attribute in page, request, session (if valid),
     * and application scope(s) in order and returns the value associated or
     * null.
     *
     * @param name the name of the attribute to search for
     * @return the value associated or null
     * @throws NullPointerException if the name is null
     */
    @Override
    public Object findAttribute(String name) {
        Objects.requireNonNull(name);
        return scopeStream()
                .sequential() // probably doesn't do anything but I want to be explicit, ensure order of search happens in order declared in stream.of
                .mapToObj(s -> getAttribute(name, s))
                .filter(v -> v != null)
                .findFirst().orElse(null);
    }


    /**
     * Remove the object reference associated with the given name
     * from all scopes.  Does nothing if there is no such object.
     *
     * @param name The name of the object to remove.
     * @throws NullPointerException if the name is null
     */
    @Override
    public void removeAttribute(String name) {
        Objects.requireNonNull(name);
        attributes.remove(name);
    }

    /**
     * Remove the object reference associated with the specified name
     * in the given scope.  Does nothing if there is no such object.
     *
     * @param name  The name of the object to remove.
     * @param scope The scope where to look.
     * @throws IllegalArgumentException if the scope is invalid
     * @throws IllegalStateException    if the scope is
     *                                  PageContext.SESSION_SCOPE but the page that was requested
     *                                  does not participate in a session or the session has been
     *                                  invalidated.
     * @throws NullPointerException     if the name is null
     */
    @Override
    public void removeAttribute(String name, int scope) {
        Objects.requireNonNull(name);
        switch(scope) {
            case PageContext.PAGE_SCOPE:
                removeAttribute(name);
                break;
            case PageContext.REQUEST_SCOPE:
                request.removeAttribute(name);
                break;
            case PageContext.SESSION_SCOPE:
                request.getSession().removeAttribute(name);
                break;
            case PageContext.APPLICATION_SCOPE:
                request.getServletContext().removeAttribute(name);
                break;
            default:
                throw new IllegalArgumentException(String.format("Invalid scope %d.", scope));
        }
    }

    /**
     * Get the scope where a given attribute is defined.
     *
     * @param name the name of the attribute to return the scope for
     * @return the scope of the object associated with the name specified or 0
     * @throws NullPointerException if the name is null
     */
    @Override
    public int getAttributesScope(String name) {
        Objects.requireNonNull(name);
        return scopeStream().sequential()
                .filter(s -> getAttribute(name, s) != null)
                .findFirst()
                .orElse(0);
    }

    /**
     * Enumerate all the attributes in a given scope.
     *
     * @param scope the scope to enumerate all the attributes for
     * @return an enumeration of names (java.lang.String) of all the
     * attributes the specified scope
     * @throws IllegalArgumentException if the scope is invalid
     * @throws IllegalStateException    if the scope is
     *                                  PageContext.SESSION_SCOPE but the page that was requested
     *                                  does not participate in a session or the session has been
     *                                  invalidated.
     */
    @Override
    public Enumeration<String> getAttributeNamesInScope(int scope) {
        switch(scope) {
            case PageContext.PAGE_SCOPE:
                return new MockHttpServletRequest.IterableEnumeration<>(attributes.keySet());
            case PageContext.REQUEST_SCOPE:
                return request.getAttributeNames();
            case PageContext.SESSION_SCOPE:
                return request.getSession().getAttributeNames();
            case PageContext.APPLICATION_SCOPE:
                return request.getServletContext().getAttributeNames();
            default:
                throw new IllegalArgumentException(String.format("Invalid scope: %d", scope));
        }
    }

    /**
     * The current value of the out object (a JspWriter).
     *
     * @return the current JspWriter stream being used for client response
     */
    @Override
    public JspWriter getOut() {
        if(out == null) {
            out = new MockJspWriter();
        }
        return out;
    }

    /**
     * Provides programmatic access to the ExpressionEvaluator.
     * The JSP Container must return a valid instance of an
     * ExpressionEvaluator that can parse EL expressions.
     *
     * @return A valid instance of an ExpressionEvaluator.
     * @since JSP 2.0
     * @deprecated As of JSP 2.1, replaced by
     */
    @Override
    public ExpressionEvaluator getExpressionEvaluator() {
        return null;
    }

    /**
     * Returns an instance of a VariableResolver that provides access to the
     * implicit objects specified in the JSP specification using this JspContext
     * as the context object.
     *
     * @return A valid instance of a VariableResolver.
     * @since JSP 2.0
     * @deprecated As of JSP 2.1, replaced by {@link javax.el.ELContext#getELResolver},
     * which can be obtained by
     * <code>jspContext.getELContext().getELResolver()</code>.
     */
    @Override
    public VariableResolver getVariableResolver() {
        return null;
    }

    /**
     * Returns the <code>ELContext</code> associated with this
     * <code>JspContext</code>.
     * <p>
     * <p>The <code>ELContext</code> is created lazily and is reused if
     * it already exists. There is a new <code>ELContext</code> for each
     * <code>JspContext</code>.</p>
     * <p>
     * <p>The <code>ELContext</code> must contain the <code>ELResolver</code>
     * described in the JSP specification (and in the javadocs for
     *
     * @return The <code>ELContext</code> associated with this
     * <code>JspContext</code>.
     * @since JSP 2.1
     */
    @Override
    public ELContext getELContext() {
        return null;
    }

    private IntStream scopeStream() {
        return IntStream.of(PageContext.PAGE_SCOPE, PageContext.REQUEST_SCOPE, PageContext.SESSION_SCOPE, PageContext.APPLICATION_SCOPE);
    }

    /**
     * <p>
     * The initialize method is called to initialize an uninitialized PageContext
     * so that it may be used by a JSP Implementation class to service an
     * incoming request and response within it's _jspService() method.
     * <p>
     * <p>
     * This method is typically called from JspFactory.getPageContext() in
     * order to initialize state.
     * <p>
     * <p>
     * This method is required to create an initial JspWriter, and associate
     * the "out" name in page scope with this newly created object.
     * <p>
     * <p>
     * This method should not be used by page  or tag library authors.
     *
     * @param servlet      The Servlet that is associated with this PageContext
     * @param request      The currently pending request for this Servlet
     * @param response     The currently pending response for this Servlet
     * @param errorPageURL The value of the errorpage attribute from the page
     *                     directive or null
     * @param needsSession The value of the session attribute from the
     *                     page directive
     * @param bufferSize   The value of the buffer attribute from the page
     *                     directive
     * @param autoFlush    The value of the autoflush attribute from the page
     *                     directive
     * @throws java.io.IOException      during creation of JspWriter
     * @throws IllegalStateException    if out not correctly initialized
     * @throws IllegalArgumentException If one of the given parameters
     *                                  is invalid
     */
    @Override
    public void initialize(Servlet servlet, ServletRequest request, ServletResponse response, String errorPageURL, boolean needsSession, int bufferSize, boolean autoFlush) throws IOException, IllegalStateException, IllegalArgumentException {

    }

    /**
     * <p>
     * This method shall "reset" the internal state of a PageContext, releasing
     * all internal references, and preparing the PageContext for potential
     * reuse by a later invocation of initialize(). This method is typically
     * called from JspFactory.releasePageContext().
     * <p>
     * <p>
     * Subclasses shall envelope this method.
     * <p>
     * <p>
     * This method should not be used by page  or tag library authors.
     */
    @Override
    public void release() {

    }

    /**
     * The current value of the session object (an HttpSession).
     *
     * @return the HttpSession for this PageContext or null
     */
    @Override
    public HttpSession getSession() {
        return request.getSession();
    }

    /**
     * The current value of the page object (In a Servlet environment,
     * this is an instance of javax.servlet.Servlet).
     *
     * @return the Page implementation class instance associated
     * with this PageContext
     */
    @Override
    public Object getPage() {
        return null;
    }

    /**
     * The current value of the request object (a ServletRequest).
     *
     * @return The ServletRequest for this PageContext
     */
    @Override
    public ServletRequest getRequest() {
        return request;
    }

    /**
     * The current value of the response object (a ServletResponse).
     *
     * @return the ServletResponse for this PageContext
     */
    @Override
    public ServletResponse getResponse() {
        return null;
    }

    /**
     * The current value of the exception object (an Exception).
     *
     * @return any exception passed to this as an errorpage
     */
    @Override
    public Exception getException() {
        return null;
    }

    /**
     * The ServletConfig instance.
     *
     * @return the ServletConfig for this PageContext
     */
    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    /**
     * The ServletContext instance.
     *
     * @return the ServletContext for this PageContext
     */
    @Override
    public ServletContext getServletContext() {
        return null;
    }

    /**
     * <p>
     * This method is used to re-direct, or "forward" the current
     * ServletRequest and ServletResponse to another active component in
     * the application.
     * </p>
     * <p>
     * If the <I> relativeUrlPath </I> begins with a "/" then the URL specified
     * is calculated relative to the DOCROOT of the <code> ServletContext </code>
     * for this JSP. If the path does not begin with a "/" then the URL
     * specified is calculated relative to the URL of the request that was
     * mapped to the calling JSP.
     * </p>
     * <p>
     * It is only valid to call this method from a <code> Thread </code>
     * executing within a <code> _jspService(...) </code> method of a JSP.
     * </p>
     * <p>
     * Once this method has been called successfully, it is illegal for the
     * calling <code> Thread </code> to attempt to modify the <code>
     * ServletResponse </code> object.  Any such attempt to do so, shall result
     * in undefined behavior. Typically, callers immediately return from
     * <code> _jspService(...) </code> after calling this method.
     * </p>
     *
     * @param relativeUrlPath specifies the relative URL path to the target
     *                        resource as described above
     * @throws IllegalStateException          if <code> ServletResponse </code> is not
     *                                        in a state where a forward can be performed
     * @throws javax.servlet.ServletException if the page that was forwarded to throws
     *                                        a ServletException
     * @throws java.io.IOException            if an I/O error occurred while forwarding
     */
    @Override
    public void forward(String relativeUrlPath) throws ServletException, IOException {
        request.getRequestDispatcher(relativeUrlPath).forward(request, getResponse());
    }

    /**
     * <p>
     * Causes the resource specified to be processed as part of the current
     * ServletRequest and ServletResponse being processed by the calling Thread.
     * The output of the target resources processing of the request is written
     * directly to the ServletResponse output stream.
     * </p>
     * <p>
     * The current JspWriter "out" for this JSP is flushed as a side-effect
     * of this call, prior to processing the include.
     * </p>
     * <p>
     * If the <I> relativeUrlPath </I> begins with a "/" then the URL specified
     * is calculated relative to the DOCROOT of the <code>ServletContext</code>
     * for this JSP. If the path does not begin with a "/" then the URL
     * specified is calculated relative to the URL of the request that was
     * mapped to the calling JSP.
     * </p>
     * <p>
     * It is only valid to call this method from a <code> Thread </code>
     * executing within a <code> _jspService(...) </code> method of a JSP.
     * </p>
     *
     * @param relativeUrlPath specifies the relative URL path to the target
     *                        resource to be included
     * @throws javax.servlet.ServletException if the page that was forwarded to throws
     *                                        a ServletException
     * @throws java.io.IOException            if an I/O error occurred while forwarding
     */
    @Override
    public void include(String relativeUrlPath) throws ServletException, IOException {
        request.getRequestDispatcher(relativeUrlPath).include(request, getResponse());
    }

    /**
     * <p>
     * Causes the resource specified to be processed as part of the current
     * ServletRequest and ServletResponse being processed by the calling Thread.
     * The output of the target resources processing of the request is written
     * directly to the current JspWriter returned by a call to getOut().
     * </p>
     * <p>
     * If flush is true, The current JspWriter "out" for this JSP
     * is flushed as a side-effect of this call, prior to processing
     * the include.  Otherwise, the JspWriter "out" is not flushed.
     * </p>
     * <p>
     * If the <i>relativeUrlPath</i> begins with a "/" then the URL specified
     * is calculated relative to the DOCROOT of the <code>ServletContext</code>
     * for this JSP. If the path does not begin with a "/" then the URL
     * specified is calculated relative to the URL of the request that was
     * mapped to the calling JSP.
     * </p>
     * <p>
     * It is only valid to call this method from a <code> Thread </code>
     * executing within a <code> _jspService(...) </code> method of a JSP.
     * </p>
     *
     * @param relativeUrlPath specifies the relative URL path to the
     *                        target resource to be included
     * @param flush           True if the JspWriter is to be flushed before the include,
     *                        or false if not.
     * @throws javax.servlet.ServletException if the page that was forwarded to throws
     *                                        a ServletException
     * @throws java.io.IOException            if an I/O error occurred while forwarding
     * @since JSP 2.0
     */
    @Override
    public void include(String relativeUrlPath, boolean flush) throws ServletException, IOException {

        request.getRequestDispatcher(relativeUrlPath).include(request, getResponse());
    }

    /**
     * <p>
     * This method is intended to process an unhandled 'page' level
     * exception by forwarding the exception to the specified
     * error page for this JSP.  If forwarding is not possible (for
     * example because the response has already been committed), an
     * implementation dependent mechanism should be used to invoke
     * the error page (e.g. "including" the error page instead).
     * <p>
     * <p>
     * If no error page is defined in the page, the exception should
     * be rethrown so that the standard servlet error handling
     * takes over.
     * <p>
     * <p>
     * A JSP implementation class shall typically clean up any local state
     * prior to invoking this and will return immediately thereafter. It is
     * illegal to generate any output to the client, or to modify any
     * ServletResponse state after invoking this call.
     * <p>
     * <p>
     * This method is kept for backwards compatiblity reasons.  Newly
     * generated code should use PageContext.handlePageException(Throwable).
     *
     * @param e the exception to be handled
     * @throws javax.servlet.ServletException if an error occurs while invoking the error page
     * @throws java.io.IOException            if an I/O error occurred while invoking the error
     *                                        page
     * @throws NullPointerException           if the exception is null
     * @see #handlePageException(Throwable)
     */
    @Override
    public void handlePageException(Exception e) throws ServletException, IOException {

    }

    /**
     * <p>
     * This method is intended to process an unhandled 'page' level
     * exception by forwarding the exception to the specified
     * error page for this JSP.  If forwarding is not possible (for
     * example because the response has already been committed), an
     * implementation dependent mechanism should be used to invoke
     * the error page (e.g. "including" the error page instead).
     * <p>
     * <p>
     * If no error page is defined in the page, the exception should
     * be rethrown so that the standard servlet error handling
     * takes over.
     * <p>
     * <p>
     * This method is intended to process an unhandled "page" level exception
     * by redirecting the exception to either the specified error page for this
     * JSP, or if none was specified, to perform some implementation dependent
     * action.
     * <p>
     * <p>
     * A JSP implementation class shall typically clean up any local state
     * prior to invoking this and will return immediately thereafter. It is
     * illegal to generate any output to the client, or to modify any
     * ServletResponse state after invoking this call.
     *
     * @param t the throwable to be handled
     * @throws javax.servlet.ServletException if an error occurs while invoking the error page
     * @throws java.io.IOException            if an I/O error occurred while invoking the error
     *                                        page
     * @throws NullPointerException           if the exception is null
     * @see #handlePageException(Exception)
     */
    @Override
    public void handlePageException(Throwable t) throws ServletException, IOException {

    }
}
