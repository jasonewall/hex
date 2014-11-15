package hex.routing;

import java.util.function.Predicate;

/**
* Created by jason on 14-11-13.
*/
public enum HttpMethod {
    GET,
    POST,
    ;
    public static final Predicate<HttpMethod> ANY = m -> true;
}
