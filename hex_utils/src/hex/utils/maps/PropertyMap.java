package hex.utils.maps;

import hex.utils.coercion.Coercible;
import hex.utils.coercion.CoercionException;
import hex.utils.generics.TypeVariables;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import static hex.utils.Inflection.toSetter;

/**
 * Created by jason on 14-12-19.
 */
public interface PropertyMap extends CoercionMap<String>, Map<String,Object>, Coercible {

    default <T> T coerce(Class<T> intoType) throws CoercionException {
        if(intoType == Object.class) {
            @SuppressWarnings("unchecked") T t = (T) this;
            return t;
        }

        try {
            T instance = newInstance(intoType);
            for(Entry<String,Object> e : entrySet()) {
                Field field = intoType.getDeclaredField(e.getKey());
                Method setter = intoType.getDeclaredMethod(toSetter(e.getKey()), field.getType());
                if(field.getType() == List.class) {
                    setter.invoke(instance, getListOf(TypeVariables.collectionTypeOf(field), e.getKey()));
                } else {
                    setter.invoke(instance, get(field.getType(), e.getKey()));
                }
            }
            return instance;
        } catch (NoSuchMethodException | NoSuchFieldException | InvocationTargetException | IllegalAccessException e) {
            throw new CoercionException(e);
        }
    }
}
