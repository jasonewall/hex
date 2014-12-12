package hex.utils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;


/**
 * Created by jason on 14-12-11.
 */
public class CoercionMapTest {
    private static class CoercionMapImpl extends HashMap<String,Object> implements CoercionMap {}

    private CoercionMap map = new CoercionMapImpl();

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
        map.getOptional("None-existant").ifPresent(
                (o) -> fail("It doesn't exist!")
        );
    }
}
