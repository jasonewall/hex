package servlet_mock;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by jason on 14-11-11.
 */
public class HttpMock {
    private static HttpMock instance;

    public enum Method {
        GET,
        POST,
    }

    public static HttpMock instance() {
        if(instance == null) {
            instance = new HttpMock();
        }

        return instance;
    }

    private String domain = "example.com";

    private String contextPath = "";

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public static Chain<HttpServletRequestHandler> GET(String path, HttpServletRequestHandler handler) {
        return instance().get(path, handler);
    }

    public static Chain<HttpServletRequestHandler> POST(String path, HttpServletRequestHandler handler) {
        return instance().post(path, handler);
    }

    public Chain<HttpServletRequestHandler> get(String path, HttpServletRequestHandler handler) {
        return doRequest(Method.GET, path, handler);
    }

    private Chain<HttpServletRequestHandler> post(String path, HttpServletRequestHandler handler) {
        return doRequest(Method.POST, path, handler);
    }

    private Chain<HttpServletRequestHandler> doRequest(Method method, String path, HttpServletRequestHandler handler) {
        HttpServletRequest request = new MockHttpServletRequest() {
            public String getMethod() {
                return method.toString();
            }

            public String getContextPath() {
                return contextPath;
            }

            public String getRequestURI() {
                return contextPath.concat(path);
            }

            public StringBuffer getRequestURL() {
                return new StringBuffer("http://").append(domain)
                        .append(contextPath)
                        .append(path);
            }
        };

        HttpServletResponse response = new MockHttpServletResponse() {};

        handler.accept(request, response);

        return (c) -> c.accept(request, response);
    }
}

