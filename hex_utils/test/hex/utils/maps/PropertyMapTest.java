package hex.utils.maps;

import hex.utils.Memo;
import hex.utils.coercion.CoercionException;
import hex.utils.test.Book;
import hex.utils.test.Person;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Created by jason on 14-12-19.
 */
public class PropertyMapTest {
    static class PropertyMapImpl extends HashMap<String,Object> implements PropertyMap {}
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

    @Test
    public void coerceShouldWorkWithNestedLists() throws CoercionException {
        map.put("title", "Dracula");
        map.put("author", Memo.of(new Person()).tap(p -> p.setName("Stoker", "Bram")).finish());
        map.put("characters", Arrays.asList(
                Memo.of(new Person()).tap(p -> p.setName("Harker", "Mina")).finish(),
                Memo.of(new Person()).tap(p -> p.setName("Van Helsing", "Abraham")).finish(),
                Memo.of(new Person()).tap(p -> p.setName("Dracula", "Count")).finish()
        ));
        Book book = map.coerce(Book.class);
        assertThat(book.getTitle(), equalTo("Dracula"));
        Memo.of(book.getCharacters().get(1)).tap(p -> {
            assertThat(p.getFirstName(), equalTo("Abraham"));
            assertThat(p.getLastName(), equalTo("Van Helsing"));
        });
    }

    @Test
    public void coerceShouldCoerceNestedLists() throws CoercionException {
        map.put("title", "Dracula");
        map.put("characters", new PropertyMap[] {
                Memo.of(new PropertyMapImpl()).tap(m -> {
                    m.put("firstName", "Mina");
                    m.put("lastName", "Harker");
                }).finish(),
                Memo.of(new PropertyMapImpl()).tap(m -> {
                    m.put("firstName", "Abraham");
                    m.put("lastName", "Van Helsing");
                }).finish()
        });
        Book book = map.coerce(Book.class);
        assertThat(book.getCharacters().size(), equalTo(2));
        Memo.of(book.getCharacters().get(0)).tap(p -> {
            assertThat(p.getFirstName(), equalTo("Mina"));
            assertThat(p.getLastName(), equalTo("Harker"));
        });
        Memo.of(book.getCharacters().get(1)).tap(p -> {
            assertThat(p.getFirstName(), equalTo("Abraham"));
            assertThat(p.getLastName(), equalTo("Van Helsing"));
        });
    }
}
