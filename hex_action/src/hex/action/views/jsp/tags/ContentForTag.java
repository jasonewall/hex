package hex.action.views.jsp.tags;

import hex.action.ControllerAction;
import hex.action.ViewContext;
import hex.jsp.ViewSupport;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.util.Objects;

/**
 * Created by jason on 14-12-25.
 */
public class ContentForTag extends BodyTagSupport {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Default processing of the start tag returning EVAL_BODY_BUFFERED.
     *
     * @return EVAL_BODY_BUFFERED
     * @throws javax.servlet.jsp.JspException if an error occurred while processing this tag
     */
    @Override
    public int doStartTag() throws JspException {
        ViewSupport.ensureViewContext(pageContext);
        return EVAL_BODY_BUFFERED;
    }

    /**
     * After the body evaluation: do not reevaluate and continue with the page.
     * By default nothing is done with the bodyContent data (if any).
     *
     * @return SKIP_BODY
     * @throws javax.servlet.jsp.JspException if an error occurred while processing this tag
     * @see #doInitBody
     */
    @Override
    public int doAfterBody() throws JspException {
        Objects.requireNonNull(name);
        getViewContext().setSectionContent(name, getBodyContent().getString());
        return SKIP_BODY;
    }

    private ViewContext getViewContext() {
        return (ViewContext)pageContext.getAttribute(ControllerAction.VIEW_CONTEXT, PageContext.REQUEST_SCOPE);
    }
}
