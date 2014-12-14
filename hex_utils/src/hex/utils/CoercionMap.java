package hex.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Created by jason on 14-12-11.
 */
public interface CoercionMap extends Map<String,Object> {
    /**
     * Formally known as get with coercion
     * Gets the value of the attribute found at {@code name} and returns it coerced into
     * {@code type}
     * @param type The type to coerce the value into
     * @param name The key of which to find the value to return
     * @return The value found at {@code name} coerced into {@code type}
     */
    default Object get(Class<?> type, String name) {
        if(type == byte.class || type == Byte.class) {
            return getByte(name);
        } else if(type == short.class || type == Short.class) {
            return getShort(name);
        } else if(type == int.class || type == Integer.class) {
            return getInt(name);
        } else if(type == float.class || type == Float.class) {
            return getFloat(name);
        } else if(type == long.class || type == Long.class) {
            return getLong(name);
        } else if(type == double.class || type == Double.class) {
            return getDouble(name);
        } else if(type == BigDecimal.class) {
            return getBigDecimal(name);
        } else if(type == BigInteger.class) {
            return getBigInteger(name);
        } else if(type == String.class) {
            return getString(name);
        } else {
            return type.cast(get(name));
        }
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

        return getNumber(attribute).map(x -> !x.equals(0))
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
