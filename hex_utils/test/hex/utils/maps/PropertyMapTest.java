package hex.utils.maps;

import hex.utils.coercion.CoercionException;
import hex.utils.test.Book;
import hex.utils.test.Person;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Created by jason on 14-12-19.
 */
public class PropertyMapTest {
    class PropertyMapImpl extends HashMap<String,Object> implements PropertyMap {}
    private PropertyMap map;

    @Before
    public void initPropertyMap() {
        map = new PropertyMapImpl();
    }

    @Test
    public void coerceShouldCopyPropertiesToAnObject() throws CoercionException {
        map.put("id", 133);
        map.put("title", "Jurassic Park");
        map.put("publishYear", 1990);

        Book book = map.coerce(Book.class);
        assertThat(book.getId(), equalTo(133));
        assertThat(book.getPublishYear(), equalTo(1990));
        assertThat(book.getTitle(), equalTo("Jurassic Park"));
    }

    @Test
    public void coerceShouldCoercePropertiesAsWell() throws CoercionException {
        map.put("id", "432");
        Book book = map.coerce(Book.class);
        assertThat(book.getId(), equalTo(432));
    }

    @Test
    public void coerceShouldWorkForNestedProperties() throws CoercionException {
        PropertyMap authorProps = new PropertyMapImpl();
        authorProps.put("id", "1");
        authorProps.put("firstName", "J.");
        authorProps.put("lastName", "Salinger");
        map.put("id", "1800");
        map.put("publishYear", "1951");
        map.put("title", "The Catcher in the Rye");
        map.put("author", authorProps);

        Book book = map.coerce(Book.class);
        Person author = book.getAuthor();
        assertThat(author.getFirstName(), equalTo("J."));
        assertThat(author.getLastName(), equalTo("Salinger"));
        assertThat(author.getId(), equalTo(1));
    }
}
