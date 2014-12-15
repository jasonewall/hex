package hex.action.params;

import hex.routing.RouteParams;
import hex.utils.maps.AbstractImmutableMap;
import hex.utils.maps.CoercionMap;

import javax.servlet.ServletRequest;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        class CoercionHashMap extends HashMap<String,Object> implements CoercionMap {}
        Map<String,String[]> reqParams = request.getParameterMap();
        Map<String,Object> entries = new HashMap<>(reqParams.size() + (routeParams.size() * 5)); // making sure we don't have to rebuild the internal hashtable, hopefully
        entries.putAll(routeParams);
        request.getParameterMap().forEach((k,v) -> {
            Pattern p = Pattern.compile("(\\w+)\\[(\\w+)\\]"); // (\w+)\[(\w+\)]
            Matcher m = p.matcher(k);
            if(m.matches()) {
                Map<String,Object> subParams = new CoercionHashMap();
                subParams.put(m.group(2), v[0]);
                entries.merge(m.group(1), subParams, (o,n) -> {
                    @SuppressWarnings("unchecked")
                    Map<String,Object> sub = (Map<String,Object>)o;
                    @SuppressWarnings("unchecked")
                    Map<String,Object> newVal = (Map<String,Object>)n;
                    sub.putAll(newVal);
                    return o;
                });
            } else {
                if(v.length == 1) entries.put(k, v[0]);
                else {
                    entries.put(k, v);
                }

            }
        });
        return entries.entrySet();
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
