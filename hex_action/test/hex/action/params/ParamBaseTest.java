package hex.action.params;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Created by jason on 14-12-10.
 */
public class ParamBaseTest {
    private static class DefaultParams extends HashMap<String,Object> implements Params {}

    private Params params;

    private List<String> allMonths;
    private List<String> with31Days;

    @Before
    public void initParamsObject() {
        params = new DefaultParams();
    }

    @Before
    public void initMonths() {
        allMonths = Arrays.asList("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec");
        with31Days = Arrays.asList("Jan", "Mar", "May", "Jul", "Aug", "Oct", "Dec");
    }

    @Test
    public void ifPresentShouldWorkInQueryChains() {
        params.put("startsWith", "J");
        Stream<String> months = Params.memoOf(allMonths.stream())
                .andThen(q -> params.ifPresent("has31Days", v -> q.filter(with31Days::contains)))
                .andThen(q -> params.ifPresent("startsWith", v -> q.filter(m -> m.startsWith(v.toString()))))
                .finish();

        List<String> results = months.collect(Collectors.toList());
        assertThat(results.size(), equalTo(3));
        assertThat(results, contains("Jan", "Jun", "Jul"));
    }
}
