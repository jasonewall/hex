package hex.utils.test.matchers;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

/**
 * Created by jason on 14-12-17.
 */
public class IntArrayMatcher extends BaseMatcher<int[]> {
    private int[] expected;

    private int[] actual;

    public IntArrayMatcher(int[] expected) {
        this.expected = expected;
    }

    @Override
    public boolean matches(Object o) {
        this.actual = (int[])o;
        for(int i : actual) {
            if(!isInExpected(i)) return false;
        }
        return true;
    }

    @Override
    public void describeTo(Description description) {

    }

    private boolean isInExpected(int i) {
        for(int e : expected) {
            if (i == e) return true;
        }
        return false;
    }

}
