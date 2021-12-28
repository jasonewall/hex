package hex.action.views;

import hex.action.views.html.HtmlElementTest;
import hex.action.views.jsp.TagTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by jason on 15-01-10.
 */
@RunWith(Suite.class) @Suite.SuiteClasses({
        TagTests.class,
        HtmlElementTest.class
})
public class ViewsSuite {
}
