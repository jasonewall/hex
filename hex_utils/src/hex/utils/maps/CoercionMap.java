package hex.utils.maps;

import hex.utils.coercion.Coercible;
import hex.utils.coercion.CoercionException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

import static hex.utils.Inflection.*;

/**
 * Created by jason on 14-12-11.
 */
public interface CoercionMap extends Map<String,Object>, Coercible {

    default <T> T coerce(Class<T> intoType) throws CoercionException {
        try {
            T instance = newInstance(intoType);
            for(Entry<String,Object> e : entrySet()) {
                Field field = intoType.getDeclaredField(e.getKey());
                Method setter = intoType.getDeclaredMethod(toSetter(e.getKey()), field.getType());
                setter.invoke(instance, get(field.getType(), e.getKey()));
            }
            return instance;
        } catch (NoSuchMethodException | NoSuchFieldException | InvocationTargetException | IllegalAccessException e) {
            throw new CoercionException(e);
        }
    }

    /**
     * Formally known as get with coercion
     * Gets the value of the attribute found at {@code name} and returns it coerced into
     * {@code type}
     * @param type The type to coerce the value into
     * @param name The key of which to find the value to return
     * @return The value found at {@code name} coerced into {@code type}
     */
    default <T> T get(Class<T> type, String name) throws CoercionException {
        Object value;
        if(type == byte.class || type == Byte.class) {
            value = getByte(name);
        } else if(type == short.class || type == Short.class) {
            value = getShort(name);
        } else if(type == int.class || type == Integer.class) {
            value = getInt(name);
        } else if(type == float.class || type == Float.class) {
            value = getFloat(name);
        } else if(type == long.class || type == Long.class) {
            value = getLong(name);
        } else if(type == double.class || type == Double.class) {
            value = getDouble(name);
        } else if(type == BigDecimal.class) {
            value = getBigDecimal(name);
        } else if(type == BigInteger.class) {
            value = getBigInteger(name);
        } else if(type == String.class) {
            value = getString(name);
        } else {
            value = get(name);
        }

        if(value instanceof Coercible) {
            Coercible c = (Coercible)value;
            return c.coerce(type);
        }
        @SuppressWarnings("unchecked")
        T t = (T)value;
        return t;
    }

    default Byte getByte(String attribute) {
        return coerceNumeric(attribute, Number::byteValue, Byte::valueOf);
    }

    default Short getShort(String attribute) {
        return coerceNumeric(attribute, Number::shortValue, Short::valueOf);
    }

    default Float getFloat(String attribute) {
        return coerceNumeric(attribute, Number::floatValue, (v,radix) -> Float.valueOf(v));
    }

    default Integer getInt(String attribute) {
        return coerceNumeric(attribute, Number::intValue, Integer::valueOf);
    }

    default Long getLong(String attribute) {
        return coerceNumeric(attribute, Number::longValue, Long::valueOf);
    }

    default Double getDouble(String attribute) {
        return coerceNumeric(attribute, Number::doubleValue, (v, radix) -> Double.valueOf(v));
    }

    default <T> T coerceNumeric(String attribute, Function<Number,T> mapper, BiFunction<String,Integer,T> coercer) {
        return getNumber(attribute).map(mapper).orElseGet(() -> coercer.apply(getString(attribute), 10));
    }

    default BigDecimal getBigDecimal(String attribute) {
        Object o = get(attribute);
        if(o instanceof BigDecimal) return (BigDecimal)o;
        String value = getString(attribute);
        if(value == null) throw new NullPointerException("BigDecimal attribute not present or value is null.");
        return new BigDecimal(value);
    }

    default BigInteger getBigInteger(String attribute) {
        Object o = get(attribute);
        if(o instanceof BigInteger) return (BigInteger)o;
        String value = getString(attribute);
        if(value == null) throw new NullPointerException("BigInteger attribute not present or value is null.");
        return new BigInteger(value);
    }

    default boolean getBool(String attribute) {
        return getBoolean(attribute);
    }

    default boolean getBoolean(String attribute) {
        Object o = get(attribute);
        if(o instanceof Boolean) return (Boolean)o;

        return getNumber(attribute).map(x -> !(x.doubleValue() == 0d))
                .orElseGet(() -> {
                    if (!containsKey(attribute)) return false;
                    String value = getString(attribute);
                    return value != null && value.length() != 0 && (
                            (value.matches("[0-9]+") && !value.equals("0"))
                                    || value.equals("true")
                                    || value.equals("yes")
                    );
                });
    }

    default String getString(String attribute) {
        Object o = get(attribute);
        if(o == null) return null;
        return o.toString();
    }

    default Optional<Number> getNumber(String attribute) {
        Object o = get(attribute);
        if(o instanceof Number) {
            return Optional.of((Number)o);
        }
        return Optional.empty();
    }

    @SuppressWarnings("unchecked")
    default <T> Optional<T> getOptional(String attribute) {
        return Optional.ofNullable((T) get(attribute));
    }
}
