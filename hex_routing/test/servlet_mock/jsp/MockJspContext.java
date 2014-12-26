package servlet_mock.jsp;

import servlet_mock.MockHttpServletRequest;

import javax.el.ELContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.el.ExpressionEvaluator;
import javax.servlet.jsp.el.VariableResolver;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * Created by jason on 14-12-23.
 */
public class MockJspContext extends JspContext {
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
}
