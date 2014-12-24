package hex.action.views.jsp.tags;

import hex.action.ControllerAction;
import hex.action.ViewContext;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

/**
 * Created by jason on 14-12-21.
 */
public class ViewContent extends SimpleTagSupport {
    @Override
    public void doTag() throws JspException, IOException {
        ViewContext view = getViewContext();
        getJspContext().getOut().print(view.getContent());
    }

    private ViewContext getViewContext() {
        return (ViewContext)getJspContext().getAttribute(ControllerAction.VIEW_CONTEXT, PageContext.REQUEST_SCOPE);
    }
}
