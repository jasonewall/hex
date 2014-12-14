package hex.action.params;

import hex.routing.RouteParams;
import org.junit.Test;
import servlet_mock.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Created by jason on 14-12-13.
 */
public class WebParamsTest {
    private HttpServletRequest webRequest;

    private MockHttpServletRequest request = new MockHttpServletRequest();

    private Map<String,String> routeParams = new HashMap<>();

    private WebParams params;

    private void buildWebParams() {
        params = new WebParams(request, new RouteParams(routeParams));
        webRequest = new HttpServletRequestWrapper(request);
        request = null;
        routeParams = null;
    }

    @Test
    public void itShouldGetParamsFromBothSources() {
        request.putParam("message", "Bond, James Bond");
        routeParams.put("id", "13");
        buildWebParams();
        assertThat(params.getInt("id"), equalTo(13));
        assertThat(params.getString("message"), equalTo("Bond, James Bond"));
    }
}
