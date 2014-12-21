package hex.utils.maps;

import hex.utils.coercion.Coercible;
import hex.utils.coercion.CoercionException;
import hex.utils.collections.CoercionArray;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Created by jason on 14-12-11.
 */
public interface CoercionMap<I> {
    Object get(Object index);

    /**
     * Formally known as get with coercion
     * Gets the value of the attribute found at {@code index} and returns it coerced into
     * {@code type}
     * @param type The type to coerce the value into
     * @param index The key of which to find the value to return
     * @return The value found at {@code index} coerced into {@code type}
     */
    default <T> T get(Class<T> type, I index) throws CoercionException {
        Object value;
        if(type == byte.class || type == Byte.class) {
            value = getByte(index);
        } else if(type == short.class || type == Short.class) {
            value = getShort(index);
        } else if(type == int.class || type == Integer.class) {
            value = getInt(index);
        } else if(type == float.class || type == Float.class) {
            value = getFloat(index);
        } else if(type == long.class || type == Long.class) {
            value = getLong(index);
        } else if(type == double.class || type == Double.class) {
            value = getDouble(index);
        } else if(type == BigDecimal.class) {
            value = getBigDecimal(index);
        } else if(type == BigInteger.class) {
            value = getBigInteger(index);
        } else if(type == String.class) {
            value = getString(index);
        } else if(type.isArray()) {
            value = coerceArray(get(index), type.getComponentType());
        } else if((value = useTypeHandlers(type, index)) == null) {
            value = get(index);
        }

        if(value instanceof Coercible) {
            Coercible c = (Coercible)value;
            return c.coerce(type);
        }
        @SuppressWarnings("unchecked")
        T t = (T)value;
        return t;
    }

    default Object useTypeHandlers(Class<?> type, I index) {
        return null;
    }

    default Byte getByte(I index) {
        return coerceNumeric(index, Number::byteValue, Byte::valueOf);
    }

    default Short getShort(I index) {
        return coerceNumeric(index, Number::shortValue, Short::valueOf);
    }

    default Float getFloat(I index) {
        return coerceNumeric(index, Number::floatValue, (v,radix) -> Float.valueOf(v));
    }

    default Integer getInt(I index) {
        return coerceNumeric(index, Number::intValue, Integer::valueOf);
    }

    default Long getLong(I index) {
        return coerceNumeric(index, Number::longValue, Long::valueOf);
    }

    default Double getDouble(I index) {
        return coerceNumeric(index, Number::doubleValue, (v, radix) -> Double.valueOf(v));
    }

    default <T> T coerceNumeric(I index, Function<Number,T> mapper, BiFunction<String,Integer,T> coercer) {
        return getNumber(index).map(mapper).orElseGet(() -> coercer.apply(getString(index), 10));
    }

    default Object coerceArray(Object array, Class<?> intoType) throws CoercionException {
        CoercionArray coercer = new CoercionArray(array);
        int length = Array.getLength(array);

        Object result = Array.newInstance(intoType, length);
        for(int i = 0; i < length; i++) {
            Array.set(result, i, coercer.get(intoType, i));
        }
        return result;
    }

    default BigDecimal getBigDecimal(I index) {
        Object o = get(index);
        if(o instanceof BigDecimal) return (BigDecimal)o;
        String value = getString(index);
        if(value == null) throw new NullPointerException("BigDecimal attribute not present or value is null.");
        return new BigDecimal(value);
    }

    default BigInteger getBigInteger(I index) {
        Object o = get(index);
        if(o instanceof BigInteger) return (BigInteger)o;
        String value = getString(index);
        if(value == null) throw new NullPointerException("BigInteger attribute not present or value is null.");
        return new BigInteger(value);
    }

    default boolean getBool(I index) {
        return getBoolean(index);
    }

    default boolean getBoolean(I index) {
        Object o = get(index);
        if(o instanceof Boolean) return (Boolean)o;

        return getNumber(index).map(x -> !(x.doubleValue() == 0d))
                .orElseGet(() -> {
                    String value = getString(index);
                    return value != null && value.length() != 0 && (
                            (value.matches("[0-9]+") && !value.equals("0"))
                                    || value.equals("true")
                                    || value.equals("yes")
                    );
                });
    }

    default String getString(I index) {
        Object o = get(index);
        if(o == null) return null;
        return o.toString();
    }

    default Optional<Number> getNumber(I index) {
        Object o = get(index);
        if(o instanceof Number) {
            return Optional.of((Number)o);
        }
        return Optional.empty();
    }

    default <T> List<T> getListOf(Class<T> elementType, I index) throws CoercionException {
        CoercionArray coercer = new CoercionArray(get(index));
        @SuppressWarnings("unchecked")
        T[] results = (T[]) Array.newInstance(elementType, coercer.length());
        for(int i = 0; i < coercer.length(); i++) {
            results[i] = coercer.get(elementType, i);
        }
        return Arrays.asList(results);
    }

    @SuppressWarnings("unchecked")
    default <T> Optional<T> getOptional(I index) {
        return Optional.ofNullable((T) get(index));
    }
}
