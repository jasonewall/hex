package hex.action;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by jason on 14-11-15.
 */
public class Controller {
    protected ViewContext view = new ViewContext();

    protected HttpServletRequest request;

    protected HttpServletResponse response;

    protected void renderText(String text) {
        try {
            response.setContentType("text/plain");
            PrintWriter writer = response.getWriter();
            writer.println(text);
            writer.flush();
        } catch (IOException e) {
            throw new ActionAbortedException(e);
        }
    }

    protected void renderPage(String pagePath) {
        //noinspection TryWithIdenticalCatches
        try {
            RequestDispatcher dispatcher = request.getRequestDispatcher(pagePath);
            dispatcher.forward(request, response);
        } catch (ServletException e) {
            throw new ActionAbortedException(e);
        } catch (IOException io) {
            throw new ActionAbortedException(io);
        }
    }
}
