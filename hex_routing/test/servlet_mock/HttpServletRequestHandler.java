package servlet_mock;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.function.BiConsumer;

/**
 * Created by jason on 14-11-15.
 */
@FunctionalInterface
public interface HttpServletRequestHandler extends BiConsumer<HttpServletRequest,HttpServletResponse> {
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException;

    default void accept(HttpServletRequest request, HttpServletResponse response) {
        try {
            handleRequest(request, response);
        } catch (IOException | ServletException e) {
            e.printStackTrace();
        }
    }
}
