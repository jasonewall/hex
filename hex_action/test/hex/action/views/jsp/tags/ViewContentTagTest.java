package hex.action.views.jsp.tags;

import hex.action.ControllerAction;
import hex.action.ViewContext;
import org.junit.Before;
import org.junit.Test;
import servlet_mock.jsp.MockJspContext;

import javax.servlet.Servlet;
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
    public void setUp() {
        view = new ViewContext();
        tag = new ViewContentTag();
    }

    @Test
    public void itShouldRenderTheViewContent() {
        view.setContent("This is action view template content");
        GET("/some_page", this::setUpRequest)
                .andThen((q,r) ->
                        assertThat(jspContext.getOut().toString(), equalTo("This is action view template content")));
    }

    @Test
    public void itShouldNotRenderTemplateContentIfSectionNameProvided() {
        view.setContent("This is action view template content");
        tag.setName("styles");
        GET("/home_page", this::setUpRequest)
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
