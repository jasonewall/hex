package hex.utils.coercion;

import hex.utils.collections.CoercionArray;

import java.lang.reflect.Array;

/**
 * Created by jason on 14-12-17.
 */
public class CoercionUtils {
    public static Object coerceArray(Object array, Class<?> intoType) throws CoercionException {
        CoercionArray coercer = new CoercionArray(array);
        int length = Array.getLength(array);

        Object result = Array.newInstance(intoType, length);
        for(int i = 0; i < length; i++) {
            Array.set(result, i, coercer.get(intoType, i));
        }
        return result;
    }
}
