package hex.action;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

/**
 * Created by jason on 14-11-15.
 */
public class Controller {
    protected ViewContext view = new ViewContext();

    protected HttpServletRequest request;

    protected HttpServletResponse response;

    protected Optional<IOException> ioException = Optional.empty();

    private Optional<ServletException> servletException = Optional.empty();

    protected void render(String text) {
        try {
            response.setContentType("text/plain");
            PrintWriter writer = response.getWriter();
            writer.println(text);
            writer.flush();
        } catch (IOException e) {
            ioException = Optional.of(e);
        }
    }

    protected void renderPage(String pagePath) {
        try {
            RequestDispatcher dispatcher = request.getRequestDispatcher(pagePath);
            dispatcher.forward(request, response);
        } catch (ServletException e) {
            servletException = Optional.of(e);
        } catch (IOException e) {
            ioException = Optional.of(e);
        }
    }
}
