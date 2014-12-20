package hex.utils.maps;

import hex.utils.coercion.Coercible;
import hex.utils.coercion.CoercionException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import static hex.utils.Inflection.*;

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
        } else {
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

    default Byte getByte(I attribute) {
        return coerceNumeric(attribute, Number::byteValue, Byte::valueOf);
    }

    default Short getShort(I attribute) {
        return coerceNumeric(attribute, Number::shortValue, Short::valueOf);
    }

    default Float getFloat(I attribute) {
        return coerceNumeric(attribute, Number::floatValue, (v,radix) -> Float.valueOf(v));
    }

    default Integer getInt(I attribute) {
        return coerceNumeric(attribute, Number::intValue, Integer::valueOf);
    }

    default Long getLong(I attribute) {
        return coerceNumeric(attribute, Number::longValue, Long::valueOf);
    }

    default Double getDouble(I attribute) {
        return coerceNumeric(attribute, Number::doubleValue, (v, radix) -> Double.valueOf(v));
    }

    default <T> T coerceNumeric(I attribute, Function<Number,T> mapper, BiFunction<String,Integer,T> coercer) {
        return getNumber(attribute).map(mapper).orElseGet(() -> coercer.apply(getString(attribute), 10));
    }

    default BigDecimal getBigDecimal(I attribute) {
        Object o = get(attribute);
        if(o instanceof BigDecimal) return (BigDecimal)o;
        String value = getString(attribute);
        if(value == null) throw new NullPointerException("BigDecimal attribute not present or value is null.");
        return new BigDecimal(value);
    }

    default BigInteger getBigInteger(I attribute) {
        Object o = get(attribute);
        if(o instanceof BigInteger) return (BigInteger)o;
        String value = getString(attribute);
        if(value == null) throw new NullPointerException("BigInteger attribute not present or value is null.");
        return new BigInteger(value);
    }

    default boolean getBool(I attribute) {
        return getBoolean(attribute);
    }

    default boolean getBoolean(I attribute) {
        Object o = get(attribute);
        if(o instanceof Boolean) return (Boolean)o;

        return getNumber(attribute).map(x -> !(x.doubleValue() == 0d))
                .orElseGet(() -> {
                    String value = getString(attribute);
                    return value != null && value.length() != 0 && (
                            (value.matches("[0-9]+") && !value.equals("0"))
                                    || value.equals("true")
                                    || value.equals("yes")
                    );
                });
    }

    default String getString(I attribute) {
        Object o = get(attribute);
        if(o == null) return null;
        return o.toString();
    }

    default Optional<Number> getNumber(I attribute) {
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
