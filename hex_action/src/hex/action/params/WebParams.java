package hex.action.params;

import hex.routing.RouteParams;
import hex.utils.maps.AbstractImmutableMap;

import javax.servlet.ServletRequest;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by jason on 14-12-12.
 */
public class WebParams extends AbstractImmutableMap<String,Object> implements Params {
    private ServletRequest request;

    private RouteParams routeParams;

    public WebParams(ServletRequest request, RouteParams routeParams) {
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
        Set<Entry<String,Object>> entries = new HashSet<>();
        entries.addAll(routeParams.entrySet());
        request.getParameterMap().forEach((k,v) -> {
            if(v.length == 1) entries.add(new SimpleImmutableEntry<>(k,v[0]));
            else {
                entries.add(new SimpleImmutableEntry<>(k,v));
            }
        });
        return entries;
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
