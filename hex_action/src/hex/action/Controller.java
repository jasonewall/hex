package hex.action;

import hex.action.params.Params;
import hex.action.views.TemplateCapturingResponse;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static hex.utils.Inflection.underscore;
/**
 * Created by jason on 14-11-15.
 */
public class Controller {
    private static final Pattern CONTROLLER_NAMES = Pattern.compile("(.*)Controller");

    protected ViewContext view = new ViewContext();

    protected HttpServletRequest request;

    protected HttpServletResponse response;

    protected Params params;

    protected String viewBase = "";

    protected boolean responseCommitted;

    /**
     * Override if conventional template directory does not work.
     * @return Name of the directory in which to find the view template.
     */
    protected String templateDirectory() {
        return getName();
    }

    String getName() {
        Matcher m = CONTROLLER_NAMES.matcher(getClass().getSimpleName());
        if(!m.matches()) throw new IllegalArgumentException(String.format(Errors.UNABLE_TO_IMPLY_VIEW_DIRECTORY, getClass().getSimpleName()));
        return underscore(m.replaceAll(m.group(1)));
    }

    public void setViewBase(String viewBase) {
        this.viewBase = viewBase;
    }

    protected String getViewPath(String forActionName) {
        String format = "html";
        String engine = "jsp";
        return new ViewPath(viewBase)
                .set(templateDirectory())
                .set(underscore(forActionName))
                .set(format)
                .set(engine)
                .toString()
                ;
    }

    protected void renderText(String text) {
        try {
            response.setContentType("text/plain");
            PrintWriter writer = response.getWriter();
            writer.println(text);
            writer.flush();
            responseCommitted = true;
        } catch (IOException e) {
            throw new ActionAbortedException(e);
        }
    }

    /**
     * Delegates directly to the servlet containers implementation of
     * {@link javax.servlet.RequestDispatcher#forward(javax.servlet.ServletRequest, javax.servlet.ServletResponse)}.
     * Probably want to use this very sparingly, as the other {@code render*} methods of controller resolve relative to your
     * views directory(ies).
     * @see {@link #renderAction(String)}
     * @see {@link #renderPath(String)}
     * @param pagePath The path of the page to render
     */
    protected void renderPage(String pagePath) {
        //noinspection TryWithIdenticalCatches
        try {
            RequestDispatcher dispatcher = request.getRequestDispatcher(pagePath);
            TemplateCapturingResponse response = new TemplateCapturingResponse(this.response);
            dispatcher.include(this.request, response);
            view.setContent(response.getContent());
            responseCommitted = true;
        } catch (ServletException e) {
            throw new ActionAbortedException(e);
        } catch (IOException io) {
            throw new ActionAbortedException(io);
        }
    }

    /**
     * Renders the view that {@code path} resolves to according to the view resolving rules,
     * including implied format and engines that normally accompany the default rendering
     * path of a typical {@link ControllerAction} request.
     * @param path The path to resolve into a view
     */
    protected void renderPath(String path) {
        Objects.requireNonNull(path);
        String[] parts = path.split("/");
        renderPage(new ViewPath(viewBase)
                .set(parts.length == 2 ? parts[0] : null)
                .set(parts.length == 2 ? parts[1] : parts[0])
                .set("html")
                .set("jsp")
                .toString())
                ;
    }

    /**
     * Renders the view that would normally be rendered had the action named by {@code actionName}
     * been called (and assumes that action does not provide it's own {@code render} call).
     *
     * e.g. Given a controller {@code PostsController} with action methods {@code update(int id)} and
     * {@code show(int id)}, where {@code update} calls {@code renderAction("show")}, the view found at
     * {@code /viewRoot/posts/html/show.jsp} will be rendered, even if the show action method explicitly renders
     * a different view.
     * @param actionName The name of the action to resolve the view for
     */
    protected void renderAction(String actionName) {
        renderPage(getViewPath(actionName));
    }
}
