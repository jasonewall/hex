package hex.utils;

import java.util.function.Function;

/**
 * Created by jason on 14-11-01.
 */
public class Inflection {
    private static final String GETTER_PREFIX = "get";
    private static final String SETTER_PREFIX = "set";

    @SafeVarargs
    public static String inflect(String string, Function<String,String>... inflectors) {
        for(Function<String,String> i : inflectors) string = i.apply(string);
        return string;
    }

    public static String capitalize(String string) {
        if(string == null || string.length() == 0) return string;
        char[] chars = string.toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);
        return new String(chars);
    }

    private static final char UNDERSCORE = '_';

    public static String underscore(String string) {
        if(string == null || string.length() == 0) return string;
        char[] chars = string.toCharArray();
        StringBuilder result = new StringBuilder();
        for(int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (Character.isUpperCase(c) ) {
                if(result.length() > 0 && Character.isLetterOrDigit(chars[i-1])) result.append(UNDERSCORE);
                result.append(Character.toLowerCase(c));
                continue;
            }
            result.append(c);
        }
        return result.toString();
    }

    public static String tableize(String string) {
        return inflect(string, Inflection::underscore, Inflection::pluralize);
    }

    public static String pluralize(String string) {
        if("Person".equals(string) || "PERSON".equals(string)) return "People";
        if("person".equals(string.toLowerCase())) return "people";
        return string.concat("s");
    }

    public static String toGetter(String fieldName) {
        return GETTER_PREFIX + capitalize(fieldName);
    }

    public static String toSetter(String fieldName) {
        return SETTER_PREFIX + capitalize(fieldName);
    }
}
