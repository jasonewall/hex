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
public class ViewContentTag extends SimpleTagSupport {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void doTag() throws JspException, IOException {
        if(name == null) {
            getJspContext().getOut().print(getViewContext().getContent());
            return;
        }
        getJspContext().getOut().print(getViewContext().getSectionContent(name));
    }

    private ViewContext getViewContext() {
        return (ViewContext)getJspContext().getAttribute(ControllerAction.VIEW_CONTEXT, PageContext.REQUEST_SCOPE);
    }
}
