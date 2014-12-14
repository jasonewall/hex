package hex.routing;

import hex.utils.Memo;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.isA;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * Created by jason on 14-12-13.
 */
public class RouteParamsTest {
    @Test
    public void getErrorsShouldBeSpecificToRouting() {
        RouteParams params = new RouteParams(
                Memo.<Map<String,String>>of(new HashMap<>())
                .tap(m -> m.put("narly", "gnarly"))
                .finish()
        );
        Stream.of(
                byte.class,
                short.class,
                int.class,
                float.class,
                long.class,
                double.class,
                Byte.class,
                Short.class,
                Integer.class,
                Float.class,
                Long.class,
                Double.class,
                BigDecimal.class,
                BigInteger.class
        ).forEach(c -> {
            try {
                params.get(c, "narly");
                fail("Expected exception for getting " + c.getSimpleName());
            } catch (IllegalArgumentException e) {
                assertThat(e, isA(IllegalArgumentException.class));
                assertThat(e.getMessage(), containsString("route parameter"));
            }
        });
    }
}
