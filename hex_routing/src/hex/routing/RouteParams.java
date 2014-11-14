package hex.routing;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jason on 14-11-13.
 */
public class RouteParams {
    private Map<String,String> params = new HashMap<>();

    protected RouteParams(Map<String,String> params) {
        this.params = params;
    }

    public int getInt(String paramName) {
        return Integer.parseInt(params.get(paramName), 10);
    }
}
