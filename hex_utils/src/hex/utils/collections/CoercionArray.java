package hex.utils.collections;

import hex.utils.maps.CoercionMap;

import java.lang.reflect.Array;

/**
 * Created by jason on 14-12-18.
 */
public class CoercionArray implements CoercionMap<Integer> {
    private final Object array;

    public CoercionArray(Object array) {
        this.array = array;
    }

    public CoercionArray(Class<?> type, int length) {
        this.array = Array.newInstance(type, length);
    }

    @Override
    public Object get(Object index) {
        return Array.get(this.array, (Integer)index);
    }
}
