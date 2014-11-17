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

    public Object get(Class<?> type, String name) throws IllegalArgumentException {
        if(type == int.class || type == Integer.class) {
            return getInt(name);
        } else {
            return getString(name);
        }
    }

    public int getInt(String paramName) throws IllegalArgumentException {
        try {
            return Integer.parseInt(params.get(paramName), 10);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(String.format("NumberFormatException in route parameter: %s", e.getMessage()), e);
        }
    }

    public String getString(String paramName) {
        return params.get(paramName);
    }
}
