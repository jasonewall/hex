package ca.thejayvm.hex.repo.utils;

import java.util.Arrays;
import java.util.function.Function;

/**
 * Created by jason on 14-11-01.
 */
public class Inflection {
    @SafeVarargs
    public static String inflect(String string, Function<String,String>... inflectors) {
        return Arrays.stream(inflectors).reduce(Function::andThen).get().apply(string);
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
        for(char c : chars) {
            if (Character.isUpperCase(c)) {
                if(result.length() > 0) result.append(UNDERSCORE);
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
        return string.concat("s");
    }
}
