package hex.action;

import hex.action.examples.PostsController;
import org.junit.Before;
import org.junit.Test;
import servlet_mock.HttpMock;
import servlet_mock.MockHttpServletResponse;

import static org.junit.Assert.*;

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

    private void assertContentType(String contentType) {
        assertEquals(contentType, controller.response.getContentType());
    }

    private void assertContent(String content) {
        assertEquals(content, ((MockHttpServletResponse) controller.response).getOutput());
    }
}
