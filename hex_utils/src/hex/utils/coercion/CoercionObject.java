package hex.utils.coercion;

import hex.utils.Inflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by jason on 15-01-03.
 */
public class CoercionObject implements CoercionMap<String> {

    private final Object source;

    public CoercionObject(Object source) {
        this.source = source;
    }

    @Override
    public Object get(Object index) {
        String propertyName = (String)index;
        try {
            Method getter = source.getClass().getMethod(Inflection.toGetter(propertyName));
            return getter.invoke(source);
        } catch (NoSuchMethodException e) {
            return null; // don't have the property, return null like a proper map would
        } catch (InvocationTargetException e) {
            Throwable x = e.getCause();
            if(x instanceof RuntimeException) {
                throw (RuntimeException)x;
            } else {
                throw new RuntimeException(String.format(Errors.PROPERTY_GETTER_THREW_EXCEPTION, propertyName), x);
            }
        } catch (IllegalAccessException e) {
            throw new UnsupportedOperationException(String.format(Errors.PROPERTY_GETTER_INSUFFICIENT_ACCESS, propertyName), e);
        }
    }
}
