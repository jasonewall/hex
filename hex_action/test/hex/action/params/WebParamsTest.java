package hex.action.params;

import hex.action.examples.Address;
import hex.action.examples.Movie;
import hex.action.examples.Person;
import hex.routing.RouteParams;
import hex.utils.Memo;
import hex.utils.coercion.CoercionException;
import hex.utils.maps.CoercionMap;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Test;
import servlet_mock.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Created by jason on 14-12-13.
 */
public class WebParamsTest {
    @SuppressWarnings({"UnusedDeclaration", "FieldCanBeLocal"})
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

    private void buildBruceWayneParams() {
        request.putParam("person[firstName]", "Bruce");
        request.putParam("person[lastName]", "Wayne");
        request.putParam("person[address][city]", "Gotham");
        request.putParam("person[address][address1]", "1007 Mountain Drive");
        request.putParam("person[address][zipCode]", "94514");
        request.putParam("person[address][state]", "NJ");

        buildWebParams();
    }

    @Test
    public void subPropertiesShouldGoAnyLevelsDeep() {
        buildBruceWayneParams();

        CoercionMap person = (CoercionMap) params.get("person");
        CoercionMap address = (CoercionMap) person.get("address");
        assertThat(address.get("city"), equalTo("Gotham"));
        assertThat(address.get("address1"), equalTo("1007 Mountain Drive"));
        assertThat(address.get("zipCode"), equalTo("94514"));
        assertThat(address.get("state"), equalTo("NJ"));
    }

    @Test
    public void nestedSubPropertiesShouldBeCoercible() throws CoercionException {
        buildBruceWayneParams();
        Person person = params.get(Person.class, "person");
        assertThat(person.getFirstName(), equalTo("Bruce"));
        assertThat(person.getLastName(), equalTo("Wayne"));
        Address address = person.getAddress();
        assertThat(address.getAddress1(), equalTo("1007 Mountain Drive"));
        assertThat(address.getCity(), equalTo("Gotham"));
        assertThat(address.getState(), equalTo("NJ"));
        assertThat(address.getZipCode(), equalTo("94514"));
    }

    @Test
    public void arrayedPropertiesNeedToWork() throws CoercionException {
        request.putParam("movie[starIds]", "14");
        request.putParam("movie[starIds]", "16");
        request.putParam("movie[starIds]", "36");
        buildWebParams();

        Movie movie = params.get(Movie.class, "movie");
        int[] starIds = movie.getStarIds();
        assertThat(starIds.length, equalTo(3));
        assertThat(starIds, new BaseMatcher<int[]>() {
            int[] expected = { 14, 16, 36 };

            int[] actual;
            @Override
            public boolean matches(Object o) {
                actual = (int[])o;
                toNextActual: for(int i : actual) {
                    for(int e : expected) {
                        if(i == e) continue toNextActual;
                    }
                    // it wasn't in this.expected
                    return false;
                }

                return true;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText(String.format("Expected all of %s to be in %s", Arrays.toString(actual), Arrays.toString(expected)));
            }
        });
    }

    @Test
    public void arrayPropertiesShouldWorkWithSubObjects() throws CoercionException {
        request.putParam("movie[title]", "The Dark Knight Rises");
        request.putParam("movie[stars][id]", "18");
        request.putParam("movie[stars][firstName]", "Anne");
        request.putParam("movie[stars][lastName]", "Hathaway");
        Person star1 = Memo.of(new Person()).tap(p -> {
            p.setId(18);
            p.setFirstName("Anne");
            p.setLastName("Hathaway");
        }).finish();
        request.putParam("movie[stars][id]", "33");
        request.putParam("movie[stars][firstName]", "Christian");
        request.putParam("movie[stars][lastName]", "Bale");
        Person star2 = Memo.of(new Person()).tap(p -> {
            p.setId(33);
            p.setFirstName("Christian");
            p.setLastName("Bale");
        }).finish();
        request.putParam("movie[stars][id]", "46");
        request.putParam("movie[stars][firstName]", "Gary");
        request.putParam("movie[stars][lastName]", "Oldman");
        Person star3 = Memo.of(new Person()).tap(p -> {
            p.setId(46);
            p.setFirstName("Gary");
            p.setLastName("Oldman");
        }).finish();
        buildWebParams();

        Movie movie = params.get(Movie.class, "movie");
        Person[] movieStars = movie.getStars();
        assertThat(movieStars, arrayContaining(star1, star2, star3));
    }

    @Test
    public void listPropertiesShouldWorkWithSubObjects() throws CoercionException {
        request.putParam("movie[title]", "The Dark Knight Rises");
        request.putParam("movie[starsList][id]", "18");
        request.putParam("movie[starsList][firstName]", "Anne");
        request.putParam("movie[starsList][lastName]", "Hathaway");
        Person star1 = Memo.of(new Person()).tap(p -> {
            p.setId(18);
            p.setFirstName("Anne");
            p.setLastName("Hathaway");
        }).finish();
        request.putParam("movie[starsList][id]", "33");
        request.putParam("movie[starsList][firstName]", "Christian");
        request.putParam("movie[starsList][lastName]", "Bale");
        Person star2 = Memo.of(new Person()).tap(p -> {
            p.setId(33);
            p.setFirstName("Christian");
            p.setLastName("Bale");
        }).finish();
        request.putParam("movie[starsList][id]", "46");
        request.putParam("movie[starsList][firstName]", "Gary");
        request.putParam("movie[starsList][lastName]", "Oldman");
        Person star3 = Memo.of(new Person()).tap(p -> {
            p.setId(46);
            p.setFirstName("Gary");
            p.setLastName("Oldman");
        }).finish();
        buildWebParams();
        Movie movie = params.get(Movie.class, "movie");
        List<Person> movieStars = movie.getStarsList();
        assertThat(movieStars, contains(star1, star2, star3));
    }
}
