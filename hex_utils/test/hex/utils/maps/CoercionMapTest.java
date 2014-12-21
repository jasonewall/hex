package hex.utils.maps;

import hex.utils.Memo;
import hex.utils.coercion.Coercible;
import hex.utils.coercion.CoercionException;
import hex.utils.test.Book;
import hex.utils.test.matchers.IntArrayMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;


/**
 * Created by jason on 14-12-11.
 */
public class CoercionMapTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    private static class CoercionMapImpl extends HashMap<String,Object> implements CoercionMap<String> {
        @Override
        public Object useTypeHandlers(Class<?> type, String index) {
            if(type == Exception.class) return "useTypeHandlers";
            return null;
        }
    }

    private CoercionMapImpl map = new CoercionMapImpl();

    @Test
    public void getIntShouldReturnInts() {
        map.put("one", 1);
        map.put("two", 2);
        map.put("tricky", 4);
        assertThat(map.getInt("one"), equalTo(1));
        assertThat(map.getInt("two"), equalTo(2));
        assertThat(map.getInt("tricky"), equalTo(4));
    }

    @Test
    public void getDoubleShouldConvert() {
        map.put("one", 1);
        assertThat(map.getDouble("one"), equalTo(1d));
    }

    @Test(expected=NullPointerException.class)
    public void getDoubleShouldThrowAppropriateExceptions() {
        map.getDouble("null");
    }

    @Test(expected=NumberFormatException.class)
    public void getDoubleShouldThrowDescriptiveExceptions() {
        map.put("thing", new ArrayList());
        map.getDouble("thing");
    }

    @Test
    public void getStringShouldConvert() {
        map.put("one", 1);
        assertThat(map.getString("one"), equalTo("1"));
    }

    @Test
    public void getStringShouldBeNullSafe() {
        assertThat(map.getString("one"), nullValue());
    }

    @Test
    public void getBooleanShouldWorkWithTruthyThings() {
        map.put("one", 1);
        assertTrue("non-zero numerics should be true", map.getBool("one"));

        map.put("one", 0);
        assertFalse("But zero should be false", map.getBool("one"));

        assertFalse("Nulls should be false", map.getBool("null")); // great for checkboxes?!

        map.put("true string", "true");
        assertTrue("Strings that are equal true should be true", map.getBool("true string"));

        map.put("numberString", "1");          assertTrue("the string '1'", map.getBool("numberString"));
        map.put("zeroString", "0");            assertFalse("the string '0'", map.getBool("zeroString"));
        map.put("emptyString", "");            assertFalse("empty string", map.getBool("emptyString"));
        map.put("numberString", "2423080234"); assertTrue("strings representing large numbers", map.getBool("numberString"));
        map.put("yes", "yes");                 assertTrue("the string 'yes'", map.getBool("yes"));
    }

    @Test
    public void getBooleanShouldWorkWithBooleanTypes() {
        map.put("boolean", true);
        assertTrue(map.getBool("boolean"));

        map.put("boolean", false);
        assertFalse(map.getBool("boolean"));
    }

    @Test
    public void getBooleanShouldNotConfuseNumericTypes() {
        map.put("number", 0f);

        assertFalse(map.getBool("number"));
    }

    @Test
    public void getBigDecimalShouldWorkWithInstancesAndStrings() {
        BigDecimal instance = BigDecimal.valueOf(3.2);
        map.put("instance", instance);
        map.put("string", "3.2");

        assertThat("if instance should return same", map.getBigDecimal("instance"), sameInstance(instance));
        assertThat("otherwise parse string", map.getBigDecimal("string"), equalTo(instance));
    }

    @Test
    public void getBigDecimalShouldRaiseNullPointerExceptionWhenMissing() {
        exception.expect(NullPointerException.class);
        exception.expectMessage("BigDecimal");
        map.getBigDecimal("null or missing attribute");
    }

    @Test
    public void getBigIntegerShouldWorkWithInstancesAndString() {
        BigInteger instance = BigInteger.valueOf(100);
        map.put("instance", instance);
        map.put("string", "100");

        assertThat("if instance should return the same", map.getBigInteger("instance"), sameInstance(instance));
        assertThat("otherwise parse string", map.getBigInteger("string"), equalTo(instance));
    }

    @Test
    public void getBigIntegerShouldRaiseNullPointerExceptionWhenMissing() {
        exception.expect(NullPointerException.class);
        exception.expectMessage("BigInteger");
        map.getBigInteger("null or missing attribute");
    }

    @Test
    public void getOptionalShouldWorkWithAVarietyOfTypes() {
        map.put("one", 1);
        assertThat(map.getOptional("one").get(), equalTo(1));

        map.put("two", "two");
        assertThat(map.getOptional("two").get(), equalTo("two"));

        map.put("double", 3d);
        assertThat(map.getOptional("double").get(), equalTo(3.0));
    }

    @Test
    public void getOptionalShouldWorkWithNulls() {
        map.getOptional("None-existent").ifPresent(
                (o) -> fail("It doesn't exist!")
        );
    }

    @Test
    public void getWithCoercionShouldWorkWithNumericEntries() {
        map.put("number", 90);
        Stream.of(
                byte.class,
                short.class,
                int.class,
                long.class,
                float.class,
                double.class,
                Byte.class,
                Short.class,
                Integer.class,
                Long.class,
                Float.class,
                Double.class,
                BigInteger.class,
                BigDecimal.class
        ).forEach(c -> {
            Object n = null;
            try {
                n = map.get(c, "number");
            } catch (CoercionException e) {
                fail(e.getMessage());
            }
            assertThat(n, instanceOf(c));
        });
    }

    @Test
    public void getWithCoercionShouldHandleCoercibleEntries() throws CoercionException {
        Coercible c = new Coercible() {
            @Override
            public <T> T coerce(Class<T> intoType) throws CoercionException {
                return null;
            }
        };
        map.put("aThing", ((Coercible) c::newInstance));
        assertThat(map.get(ArrayList.class, "aThing"), isA(ArrayList.class));
    }

    @Test
    public void getWithCoercionShouldWorkWithArrays() throws CoercionException {
        map.put("ids", new int[]{ 44, 21, 83, 98 });
        int[] ids = map.get(int[].class, "ids");
        assertThat(ids.length, equalTo(4));
        assertThat(ids, new IntArrayMatcher(new int[]{ 44, 21, 83, 98 }));
    }

    @Test
    public void getWithCoercionShouldCoerceArrayElements() throws CoercionException {
        map.put("ids", new String[]{ "23", "89" });
        int[] ids = map.get(int[].class, "ids");
        assertThat(ids.length, equalTo(2));
        assertThat(ids, new IntArrayMatcher(new int[]{ 23, 89 }));
    }

    private void initBookAttributes() {
        Object[] bookAttributes = {
                Memo.of(new PropertyMapTest.PropertyMapImpl()).tap(m -> {
                    m.put("id", 38);
                    m.put("title", "To Kill A Mocking Bird");
                }).finish(),
                Memo.of(new PropertyMapTest.PropertyMapImpl()).tap(m -> {
                    m.put("id", 19);
                    m.put("title", "Dracula");
                }).finish()
        };
        map.put("books", bookAttributes);
    }

    @Test
    public void getWithCoercionShouldCoerceComplexArrayElements() throws CoercionException {
        initBookAttributes();
        Book[] books = map.get(Book[].class, "books");
        assertThat(books[0].getId(), equalTo(38));
        assertThat(books[0].getTitle(), equalTo("To Kill A Mocking Bird"));
        assertThat(books[1].getId(), equalTo(19));
        assertThat(books[1].getTitle(), equalTo("Dracula"));
    }

    @Test
    public void getWithCoercionShouldCheckTypeHandlers() throws CoercionException {
        assertThat(map.get(Exception.class, "blah"), equalTo("useTypeHandlers"));
    }

    @Test
    public void gettingArraysShouldWorkWithSingleValues() throws CoercionException {
        map.put("names", "Bob");
        String[] names = map.get(String[].class, "names");
        assertThat(names, arrayContaining("Bob"));
    }

    @Test
    public void getListOfShouldReturnAList() throws CoercionException {
        map.put("names", new String[]{ "Darla", "Dawn", "David", "Donald" });
        List<String> names = map.getListOf(String.class, "names");
        assertThat(names, contains("Darla", "Dawn", "David", "Donald"));
    }

    @Test
    public void getListOfShouldWorkWithSingleValues() throws CoercionException {
        map.put("names", "Dylan");
        List<String> names = map.getListOf(String.class, "names");
        assertThat(names, contains("Dylan"));
    }
}
