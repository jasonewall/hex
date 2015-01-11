package hex.action.views.jsp.tags;

import hex.action.routing.Uri;
import hex.action.routing.UriFactory;
import hex.action.views.html.HtmlElement;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.io.IOException;

/**
 * Created by jason on 15-01-03.
 */
public class LinkTag extends BodyTagSupport {
    private String actionName;

    private String anchor;

    private Object values;

    public void setAction(String actionName) {
        this.actionName = actionName;
    }

    public void setAnchor(String anchor) {
        this.anchor = anchor;
    }

    public void setTo(Object values) {
        this.values = values;
    }

    private Uri uri;

    public Uri getUri() {
        return uri;
    }

    /**
     * Default processing of the start tag, returning SKIP_BODY.
     *
     * @return SKIP_BODY
     * @throws javax.servlet.jsp.JspException if an error occurs while processing this tag
     */
    @Override
    public int doStartTag() throws JspException {
        uri = UriFactory.getUrlFor(actionName, pageContext.getRequest(), values);
        uri.setAnchor(anchor);
        return EVAL_BODY_BUFFERED;
    }

    /**
     * Default processing of the end tag returning EVAL_PAGE.
     *
     * @return EVAL_PAGE
     * @throws javax.servlet.jsp.JspException if an error occurs while processing this tag
     */
    @Override
    public int doEndTag() throws JspException {
        HtmlElement element = new HtmlElement("a");
        element.setAttribute("href", uri.toString());
        element.setBody(getBodyContent().getString());
        try {
            element.write(pageContext.getOut());
        } catch (IOException e) {
            throw new JspException("Error while writing html element", e);
        }
        return EVAL_PAGE;
    }
}
