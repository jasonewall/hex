package hex.action;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static hex.utils.utils.Inflection.underscore;
/**
 * Created by jason on 14-11-15.
 */
public class Controller {
    protected ViewContext view = new ViewContext();

    protected HttpServletRequest request;

    protected HttpServletResponse response;

    private static final Pattern CONTROLLER_NAMES = Pattern.compile("(.*)Controller");

    private String templateDirectory() {
        Matcher m = CONTROLLER_NAMES.matcher(getClass().getSimpleName());
        if(!m.matches()) throw new IllegalArgumentException(String.format(Errors.UNABLE_TO_IMPLY_VIEW_DIRECTORY, getClass().getSimpleName()));
        return underscore(m.replaceAll(m.group(1)));
    }

    protected String getViewPath(String forActionName) {
        String format = "html";
        String engine = "jsp";
        return new ViewPath()
                .set(templateDirectory())
                .set(format)
                .set(underscore(forActionName))
                .set(engine);
    }

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
