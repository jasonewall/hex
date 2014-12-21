package hex.action.params;

import hex.routing.RouteParams;

import javax.servlet.ServletRequest;
import java.util.Set;

/**
 * Created by jason on 14-12-12.
 */
public class WebParams extends ParameterMap {
    private ServletRequest request;

    private RouteParams routeParams;

    public WebParams(ServletRequest request, RouteParams routeParams) {
        super(request.getParameterMap().size() + (routeParams.size() * 5));
        this.request = request;
        this.routeParams = routeParams;
    }

    /**
     * Build the {@code entrySet} that will be converted into an immutable implementation of the {@link java.util.Set}
     * interface.
     * <p>
     * This pattern is meant to be a convenience for creating implementations of an immutable {@link java.util.Map}
     * implementation.
     *
     * @return A {@link java.util.Set} of entries
     */
    @Override
    protected Set<Entry<String, Object>> buildEntries() {
        params.putAll(routeParams);

        request.getParameterMap().forEach((k,v) -> {
            if(v.length == 1) params.put(k,v[0]);
            else params.put(k,v);
        });

        return super.buildEntries();
    }

    /**
     * @throws UnsupportedOperationException The <tt>clear</tt> operation is not supported by this class.
     */
    @Override
    public void clear() {
        throw new UnsupportedOperationException("WebParams are immutable.");
    }

    /**
     * @throws UnsupportedOperationException The <tt>remove</tt> operation is not supported by this class.
     */
    @Override
    public Object remove(Object key) {
        throw new UnsupportedOperationException("WebParams are immutable.");
    }

    /**
     * @throws UnsupportedOperationException The <tt>put</tt> operation is not supported by this class.
     */
    @Override
    public Object put(String key, Object value) {
        throw new UnsupportedOperationException("WebParams are immutable.");
    }
}
