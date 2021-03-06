package hex.action;

import hex.action.examples.PostsController;
import org.junit.Before;
import org.junit.Test;
import servlet_mock.HttpMock;
import servlet_mock.MockHttpServletRequest;
import servlet_mock.MockHttpServletResponse;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Created by jason on 14-11-16.
 */
public class ControllerTest {
    private Controller controller;

    @Before
    public void initController() {
        controller = new PostsController();
        HttpMock.GET("/", (q,r) -> {
            controller.request = q;
            controller.response = r;
        });
    }

    @Test
    public void renderTextShouldRender() {
        controller.renderText("This too shall pass.");
        assertContentType("text/plain");
        assertContent("This too shall pass.\n");
    }

    @Test
    public void renderPageShouldForwardToSomethingNew() {
        controller.renderPage("blogs_list.jsp");
        assertContentType("text/html");
        assertRenderedPage("blogs_list.jsp");
    }

    @Test
    public void renderActionShouldObeyViewPathRules() {
        controller.renderAction("homeBoy");
        assertContentType("text/html");
        assertRenderedPage("/posts/home_boy.html.jsp");
    }

    @Test
    public void renderPathShouldGoWhereItIsTold() {
        controller.renderPath("comments/index");
        assertContentType("text/html");
        assertRenderedPage("/comments/index.html.jsp");
    }

    @Test
    public void renderPathShouldWorkForRootPages() {
        controller.renderPath("main");
        assertContentType("text/html");
        assertRenderedPage("/main.html.jsp");
    }

    private void assertContentType(String contentType) {
        assertEquals(contentType, controller.response.getContentType());
    }

    private void assertContent(String content) {
        assertEquals(content, ((MockHttpServletResponse) controller.response).getOutput());
    }

    private void assertRenderedPage(String page) {
        assertThat(((MockHttpServletRequest)controller.request).getIncludedPages(), contains(page));
    }
}
