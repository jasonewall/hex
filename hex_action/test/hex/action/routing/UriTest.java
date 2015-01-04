package hex.action.routing;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by jason on 15-01-04.
 */
public class UriTest {
    @Test
    public void queryParamsShouldBeFormattedCorrectly() {
        Uri uri = new Uri();
        uri.setPath("/cowboys");
        uri.addQueryParam("and", "aliens");
        assertThat(uri.toString(), is("/cowboys?and=aliens"));
    }

    @Test
    public void multipleQueryParamsShouldBeCool() {
        Uri uri = new Uri();
        uri.setPath("/posts");
        uri.addQueryParam("tag", "interesting");
        uri.addQueryParam("title", "Atlantis");
        assertThat(uri.toString(), is("/posts?tag=interesting&title=Atlantis"));
    }
}
