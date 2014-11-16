package hex.action;

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
}
