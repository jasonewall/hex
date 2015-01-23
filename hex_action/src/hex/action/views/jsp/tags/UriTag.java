package hex.action.views.jsp.tags;

import hex.action.routing.Uri;

import java.util.Map;

/**
 * Created by jason on 15-01-16.
 */
public interface UriTag {
    void setQueryParam(String name, String value);

    void setParam(String name, String value);
}
