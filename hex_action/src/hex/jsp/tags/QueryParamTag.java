package hex.jsp.tags;

import javax.servlet.jsp.JspException;
import java.io.IOException;
import java.util.Objects;

/**
 * Created by jason on 15-01-07.
 */
public class QueryParamTag extends ParamTag {

    /**
     * Default processing of the tag does nothing.
     *
     * @throws javax.servlet.jsp.JspException      Subclasses can throw JspException to indicate
     *                                             an error occurred while processing this tag.
     * @throws javax.servlet.jsp.SkipPageException If the page that
     *                                             (either directly or indirectly) invoked this tag is to
     *                                             cease evaluation.  A Simple Tag Handler generated from a
     *                                             tag file must throw this exception if an invoked Classic
     *                                             Tag Handler returned SKIP_PAGE or if an invoked Simple
     *                                             Tag Handler threw SkipPageException or if an invoked Jsp Fragment
     *                                             threw a SkipPageException.
     * @throws java.io.IOException                 Subclasses can throw IOException if there was
     *                                             an error writing to the output stream
     */
    @Override
    public void doTag() throws JspException, IOException {
        UriTag tag = (UriTag)findAncestorWithClass(this, UriTag.class);
        Objects.requireNonNull(tag);
        tag.setQueryParam(name, value);
    }
}
