package hex.utils.collections;

import hex.utils.coercion.CoercionMap;

import java.lang.reflect.Array;

/**
 * Created by jason on 14-12-18.
 */
public class CoercionArray implements CoercionMap<Integer> {
    private final Object array;

    private final int length;

    public CoercionArray(Object array) {
        this.array = array;
        this.length = Array.getLength(array);
    }

    public CoercionArray(Class<?> type, int length) {
        this.array = Array.newInstance(type, length);
        this.length = length;
    }

    @Override
    public Object get(Object index) {
        return Array.get(this.array, (Integer)index);
    }

    public void set(int index, Object value) {
        Array.set(array, index, value);
    }

    public int length() {
        return length;
    }
}
