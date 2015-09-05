package hex.jsp.tags;

/**
 * Created by jason on 15-01-16.
 */
public interface UriTag {
    void setQueryParam(String name, String value);

    void setParam(String name, String value);
}
