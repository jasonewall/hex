package hex.utils;

import hex.utils.maps.CoercionMapTest;
import hex.utils.maps.PropertyMapTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        InflectionTest.class,
        PropertyMapTest.class,
        CoercionMapTest.class
})
public class UtilsTests {
}
