package hex.action;

import hex.utils.maps.CoercionMap;

import java.util.HashMap;

/**
 * Created by jason on 14-11-15.
 */
public class ViewContext extends HashMap<String,Object> implements CoercionMap<String> {
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {

        this.content = content;
    }
}
