package hex.action.views.html;

import org.hamcrest.Matchers;
import org.junit.Test;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by jason on 15-01-10.
 */
public class HtmlElementTest {
    private HtmlElement tag;

    private void setTag(String tagName) {
        this.tag = new HtmlElement(tagName);
    }

    @Test
    public void shouldRenderHtml() {
        setTag("a");
        tag.setBody("Home");
        assertThat(tag.toString(), is("<a>Home</a>"));
    }

    @Test
    public void shouldSelfCloseWhenAppropriate() {
        setTag("rara");
        assertThat(tag.toString(), is("<rara/>"));
    }

    @Test
    public void shouldIncludeAttributesWithSelfClosing() {
        setTag("input");
        tag.setAttribute("type", "text");
        tag.setAttribute("name", "userName");
        String tagHtml = tag.toString();
        assertThat(tagHtml, containsString("<input"));
        assertThat(tagHtml, containsString("type=\"text\""));
        assertThat(tagHtml, containsString("name=\"userName\""));
        assertThat(tagHtml, containsString("/>"));
    }

    @Test
    public void shouldIncludeAttributesInBodyTag() {
        setTag("form");
        tag.setAttribute("method", "POST");
        tag.setAttribute("action", "/people");
        tag.setBody("some form html");
        String tagHtml = tag.toString();
        assertThat(tagHtml, containsString("<form"));
        assertThat(tagHtml, containsString("method=\"POST\""));
        assertThat(tagHtml, containsString(">some form html<"));
        assertThat(tagHtml, containsString("</form>"));
    }
}
