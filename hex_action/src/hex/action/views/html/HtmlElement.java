package hex.action.views.html;

import javax.servlet.jsp.JspWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.*;

/**
 * Created by jason on 15-01-10.
 */
public class HtmlElement {
    private static final char LT = '<';
    private static final char GT = '>';
    private static final char CS = '/';
    private static final char SPACE = ' ';
    private static final char EQ = '=';
    private static final char QUOTE = '"';

    private String tagName;

    private String body;

    private final Map<String,String> attributes = new LinkedHashMap<>();

    public HtmlElement(String tagName) {
        this.tagName = tagName;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String toString() {
        StringWriter out = new StringWriter();
        try {
            write(out);
        } catch (IOException e) {
            // StringWriter won't throw exception
            // And if it does.... whatever...
        }
        return out.toString();
    }

    public HtmlElement setAttribute(String name, String value) {
        attributes.put(name, value);
        return this;
    }

    public void removeAttribute(String name) {
        attributes.remove(name);
    }

    public String getAttribute(String name) {
        return attributes.get(name);
    }

    private Writer quote(String value, Writer to) throws IOException {
        return to.append(QUOTE).append(value).append(QUOTE);
    }

    public void write(Writer out) throws IOException {
        List<IOException> errors = new ArrayList<>();
        out.append(LT).append(tagName);
        if(attributes.size() > 0) {
            attributes.forEach((k,v) -> {
                try {
                    out.append(SPACE).append(k).append(EQ);
                    quote(v, out);
                } catch (IOException e) {
                    errors.add(e);
                }
            });
            if(errors.size() > 0) throw errors.get(0);
        }
        if(body == null) {
            out.append(CS).append(GT);
        } else {
            out.append(GT).append(body).append(LT).append(CS).append(tagName).append(GT);
        }
    }
}
