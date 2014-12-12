package hex.action.params;

import hex.utils.Memo;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * Created by jason on 14-12-09.
 */
public interface Params extends Map<String,Object> {
    public static <T> Memo<T> memoOf(T t) {
        return Memo.of(t);
    }

    default int getInt(String attribute) {
        return (int)get(attribute);
    }

    default double getDouble(String attribute) {

        return Double.valueOf(get(attribute));
    }

    default <R> R ifPresent(String attribute, Function<Object,R> action) {
        if(containsKey(attribute)) {
            return action.apply(get(attribute));
        }
        return null;
    }

    default String getString(String attribute) {
        Object o = get(attribute);
        if(o == null) return null;
        return o.toString();
    }

    @SuppressWarnings("unchecked")
    default <T> Optional<T> getOptional(String attribute) {
        return Optional.ofNullable((T)get(attribute));
    }
}
