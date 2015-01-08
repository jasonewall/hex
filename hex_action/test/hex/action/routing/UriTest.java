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

    @Test
    public void queryStringAndAnchor() {
        Uri uri = new Uri();
        uri.setPath("/aliens");
        uri.addQueryParam("encounterYear", "1947");
        uri.setAnchor("list");
        assertThat(uri.toString(), is("/aliens?encounterYear=1947#list"));
    }

    @Test
    public void onceMoreWithFeeling() {
        Uri uri = Uri.create("https", "example.com", 3000);
        uri.setPath("/roswell");
        uri.addQueryParam("recordYear", "1947");
        uri.setAnchor("conspiracies");
        assertThat(uri.toString(), is("https://example.com:3000/roswell?recordYear=1947#conspiracies"));
    }
}
