package hex.action.params;

import hex.utils.CoercionMap;
import hex.utils.Memo;

import java.util.function.Function;

/**
 * Created by jason on 14-12-09.
 */
public interface Params extends CoercionMap {
    public static <T> Memo<T> memoOf(T t) {
        return Memo.of(t);
    }

    default <R> R ifPresent(String attribute, Function<Object,R> action) {
        if(containsKey(attribute)) {
            return action.apply(get(attribute));
        }
        return null;
    }

}
