package hex.action.views.jsp.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jason on 15-01-24.
 */
public abstract class HexActionTagBase extends BodyTagSupport implements UriTag {
    protected String actionName;

    protected Object values;

    protected Map<String,String> params;

    private Map<String,String> queryParams;

    public void setAction(String actionName) {
        this.actionName = actionName;
    }

    public void setValues(Object values) {
        this.values = values;
    }

    @Override
    public void setQueryParam(String name, String value) {
        queryParams.put(name, value);
    }

    public void setParam(String name, String value) {
        params.put(name, value);
    }

    /**
     * Default processing of the start tag, returning SKIP_BODY.
     *
     * @return SKIP_BODY
     * @throws javax.servlet.jsp.JspException if an error occurs while processing this tag
     */
    @Override
    public int doStartTag() throws JspException {
        params = new HashMap<>();
        queryParams = new HashMap<>();
        return EVAL_BODY_BUFFERED;
    }
}
