package ca.thejayvm.hex.repo.utils;

import org.junit.Test;

import static org.junit.Assert.*;
import static ca.thejayvm.hex.repo.utils.Inflection.*;

/**
 * Created by jason on 14-11-01.
 */
public class InflectionTest {
    @Test
    public void capitalizeIsOkWithWhatever() {
        assertNull("Null should return null", capitalize(null));
        assertEquals("Empty string should return empty string", "", capitalize(""));
        assertEquals("Whitespace should just work", "   ", capitalize("   "));
    }

    @Test
    public void capitalizeCapitalizesTheFirstLetter() {
        assertEquals("First letter should get capitalized", "Apple", capitalize("apple"));
        assertEquals("And only the first letter should get capitalized", "I like apples, do you?", capitalize("i like apples, do you?"));
    }

    @Test
    public void underscoreSplitsOnCapitals() {
        assertEquals("firstName", "first_name", underscore("firstName"));
    }

    @Test
    public void underscoreDoesNotStartWithUnderscore() {
        assertEquals("If first letter is capital, do not append _", "last_name", underscore("LastName"));
    }

    @Test
    public void underscoreIsOkWithEdgeCases() {
        assertNull("Null should return null", underscore(null));
        assertEquals("Empty string should return empty string", "", underscore(""));
        assertEquals("White space should just work", "    ", underscore(("    ")));
    }
}
