package hex.action;

import java.util.HashMap;

/**
 * Created by jason on 14-11-15.
 */
public class ViewContext extends HashMap<String,Object> {
    public boolean getBoolean(String attribute) {
        return (boolean)get(attribute);
    }

    public int getInt(String attribute) {
        return (int)get(attribute);
    }
}
