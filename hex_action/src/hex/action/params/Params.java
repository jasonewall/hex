package hex.action.params;

import hex.utils.Memo;
import hex.utils.maps.PropertyMap;

import java.util.function.Function;

/**
 * Created by jason on 14-12-09.
 */
public interface Params extends PropertyMap {
    static <T> Memo<T> memoOf(T t) {
        return Memo.of(t);
    }

    default <R> R ifPresent(String attribute, Function<Object,R> action) {
        if(containsKey(attribute)) {
            return action.apply(get(attribute));
        }
        return null;
    }

}
