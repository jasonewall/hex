package hex.action.routing;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
* Created by jason on 15-01-04.
*/
class Uri {
    private Optional<String> scheme = Optional.empty();

    private Optional <String> host = Optional.empty();

    private int port;

    private Optional<String> context = Optional.empty();

    private Optional<String> path = Optional.empty();

    private Optional<String> anchor = Optional.empty();

    private Map<String,String> queryParams = new LinkedHashMap<>();

    public static Uri create(String scheme, String host, int port) {
        Uri uri = new Uri();
        uri.scheme = Optional.of(scheme);
        uri.host = Optional.of(host);
        uri.port = port;
        return uri;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        scheme.ifPresent(s -> builder.append(s).append("://"));
        host.ifPresent(builder::append);
        if(port != 0 && port != 80) builder.append(':').append(port);
        context.ifPresent(builder::append);
        path.ifPresent(builder::append);
        if(queryParams.size() > 0) {
            builder.append('?');
            builder.append(queryParams.entrySet().stream().map(Object::toString).collect(Collectors.joining("&")));
        }
        anchor.ifPresent(s -> builder.append('#').append(s));
        return builder.toString();
    }

    public void addQueryParam(String name, String value) {
        try {
            queryParams.put(name, URLEncoder.encode(value, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            // hardcoded UTF-8
            // TODO: Log a warning here, just in case.
        }
    }

    public void setContext(String context) {
        this.context = Optional.ofNullable(context);
    }

    public void setPath(String path) {
        this.path = Optional.ofNullable(path);
    }

    public void setAnchor(String anchorValue) {
        this.anchor = Optional.ofNullable(anchorValue);
    }
}
