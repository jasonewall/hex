package hex.routing;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jason on 14-11-13.
 */
public class RouteParams {
    private Map<String,String> params = new HashMap<>();

    public RouteParams(Map<String,String> params) {
        this.params = params;
    }

    public Object get(Class<?> type, String name) {
        if(type == int.class || type == Integer.class) {
            return getInt(name);
        } else {
            return getString(name);
        }
    }

    public int getInt(String paramName) {
        return Integer.parseInt(params.get(paramName), 10);
    }

    public String getString(String paramName) {
        return params.get(paramName);
    }
}
