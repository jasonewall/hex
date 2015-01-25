package hex.action.views.jsp.tags;

import hex.action.ControllerAction;
import hex.action.ViewContext;
import org.junit.Before;
import org.junit.Test;
import servlet_mock.jsp.MockJspContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import java.io.IOException;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static servlet_mock.HttpMock.GET;

/**
 * Created by jason on 14-12-23.
 */
public class ViewContentTagTest {
    protected ViewContext view;

    protected ViewContentTag tag;

    protected MockJspContext jspContext;

    @Before
    public void setUpViewContext() {
        view = new ViewContext();
    }

    @Before
    public void setUp() {
        tag = new ViewContentTag();
        view.setContent("This is action view template content");
    }

    @Test
    public void itShouldRenderTheViewContent() {
        GET("/some_page", this::setUpRequest)
                .andThen((q,r) ->
                        assertThat(jspContext.getOut().toString(), equalTo("This is action view template content")));
    }

    @Test
    public void itShouldNotRenderTemplateContentIfSectionNameProvided() {
        tag.setName("styles");
        GET("/home_page", this::setUpRequest)
                .andThen((q,r) ->
                        assertThat(jspContext.getOut().toString(), equalTo("")));
    }

    @Test
    public void itShouldRenderSectionContent() {
        tag.setName("footer");
        view.setSectionContent("footer", "This is the footer section content");
        GET("/awesome_index", this::setUpRequest)
                .andThen((q,r) ->
                        assertThat(jspContext.getOut().toString(), equalTo("This is the footer section content")));
    }

    @Test
    public void shouldNotDumpNullIntoAPage() {
        view.setContent(null);
        GET("/welcome", this::setUpRequest)
                .andThen((q,r) ->
                        assertThat(jspContext.getOut().toString(), equalTo("")));
    }

    private void setUpRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setAttribute(ControllerAction.VIEW_CONTEXT, view);
        jspContext = new MockJspContext(request);
        tag.setJspContext(jspContext);
        try {
            tag.doTag();
        } catch (JspException e) {
            throw new ServletException(e);
        }
        jspContext.getOut().flush();
    }
}
