package hex.action.views.jsp.tags;

import hex.action.routing.Uri;
import hex.action.routing.UriFactory;
import hex.action.views.html.HtmlElement;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jason on 15-01-03.
 */
public class LinkTag extends HexActionTagBase {

    private String anchor;

    public void setAnchor(String anchor) {
        this.anchor = anchor;
    }

    public void setTo(Object values) {
        setValues(values);
    }

    /**
     * Default processing of the end tag returning EVAL_PAGE.
     *
     * @return EVAL_PAGE
     * @throws javax.servlet.jsp.JspException if an error occurs while processing this tag
     */
    @Override
    public int doEndTag() throws JspException {
        Uri uri = UriFactory.getUrlFor(actionName, pageContext.getRequest(),
                params.size() > 0 ? params : values);
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
