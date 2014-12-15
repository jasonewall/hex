package hex.action.params;

import hex.action.examples.Movie;
import hex.routing.RouteParams;
import hex.utils.coercion.CoercionException;
import hex.utils.maps.CoercionMap;
import org.junit.Test;
import servlet_mock.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.contains;
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

    @Test
    public void itShouldBeAwareOfSubProperties() {
        request.putParam("person[firstName]", "James");
        request.putParam("person[lastName]", "Bond");
        buildWebParams();
        Object o = params.get("person");
        Map person = (Map)o;
        assertThat(person.get("firstName"), equalTo("James"));
        assertThat(person.get("lastName"), equalTo("Bond"));
    }

    @Test
    public void subPropertiesShouldBeCoercible() throws CoercionException {
        request.putParam("movie[refid]", "tt0055928");
        request.putParam("movie[title]", "Dr. No");
        request.putParam("movie[releaseYear]", "1962");
        request.putParam("movie[director]", "Terence Young");

        buildWebParams();

        Movie movie = params.get(Movie.class, "movie");
        assertThat(movie.getDirector(), equalTo("Terence Young"));
        assertThat(movie.getRefid(), equalTo("tt0055928"));
        assertThat(movie.getReleaseYear(), equalTo(1962));
        assertThat(movie.getTitle(), equalTo("Dr. No"));
    }

    @Test
    public void subPropertiesShouldGoAnyLevelsDeep() {
        request.putParam("person[firstName]", "Bruce");
        request.putParam("person[lastName]", "Wayne");
        request.putParam("person[address][city]", "Gotham");
        request.putParam("person[address][address1]", "1007 Mountain Drive");
        request.putParam("person[address][zipcode]", "94514");
        request.putParam("person[address][state]", "NJ");

        buildWebParams();

        CoercionMap person = (CoercionMap) params.get("person");
        CoercionMap address = (CoercionMap) person.get("address");
        assertThat(address.get("city"), equalTo("Gotham"));
        assertThat(address.get("address1"), equalTo("1007 Mountain Drive"));
        assertThat(address.get("zipcode"), equalTo("94514"));
        assertThat(address.get("state"), equalTo("NJ"));
    }
}
