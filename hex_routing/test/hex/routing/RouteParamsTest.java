package hex.routing;

import hex.utils.Memo;
import hex.utils.coercion.CoercionException;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * Created by jason on 14-12-13.
 */
public class RouteParamsTest {
    private final String paramName = "narly";
    private RouteParams params = new RouteParams(
                Memo.<Map<String,String>>of(new HashMap<>())
                .tap(m -> m.put(paramName, "gnarly"))
                .finish()
        );

    @Test
    public void getErrorsShouldBeSpecificToRouting() {

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
        ).forEach(c -> assertCall(c.getSimpleName(), () -> {
            try {
                params.get(c, paramName);
            } catch (CoercionException e) {
                fail(e.getMessage());
            }
        }));
    }

    @Test
    public void getByteErrorsShouldReferenceRouting() {
        assertCall("from getByte", () -> params.getByte(paramName));
    }

    @Test
    public void getShortErrorsShouldReferenceRouting() {
        assertCall("from getShort", () -> params.getShort(paramName));
    }

    @Test
    public void getIntErrorsShouldReferenceRouting() {
        assertCall("from getInt", () -> params.getInt(paramName));
    }
    @Test
    public void getFloatErrorsShouldReferenceRouting() {
        assertCall("from getFloat", () -> params.getFloat(paramName));
    }
    @Test
    public void getLongErrorsShouldReferenceRouting() {
        assertCall("from getLong", () -> params.getLong(paramName));
    }

    @Test
    public void getDoubleErrorsShouldReferenceRouting() {
        assertCall("from getDouble", () -> params.getDouble(paramName));
    }

    private void assertCall(String message, Runnable runner) {
        try {
            runner.run();
            fail("Expected exception for getting " + message);
        } catch(IllegalArgumentException e) {
            assertException(e);
        }
    }

    private void assertException(IllegalArgumentException e) {
        assertThat(e.getMessage(), containsString("route parameter"));
    }
}
