package hex.utils;

import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Created by jason on 14-12-11.
 */
public interface CoercionMap extends Map<String,Object> {
    default byte getByte(String attribute) {
        return coerceNumeric(attribute, Number::byteValue, Byte::valueOf);
    }

    default short getShort(String attribute) {
        return coerceNumeric(attribute, Number::shortValue, Short::valueOf);
    }

    default float getFloat(String attribute) {
        return coerceNumeric(attribute, Number::floatValue, (v,radix) -> Float.valueOf(v));
    }

    default int getInt(String attribute) {
        return coerceNumeric(attribute, Number::intValue, Integer::valueOf);
    }

    default long getLong(String attribute) {
        return coerceNumeric(attribute, Number::longValue, Long::valueOf);
    }

    default double getDouble(String attribute) {
        return coerceNumeric(attribute, Number::doubleValue, (v, radix) -> Double.valueOf(v));
    }

    default <T> T coerceNumeric(String attribute, Function<Number,T> mapper, BiFunction<String,Integer,T> coercer) {
        return getNumber(attribute).map(mapper).orElseGet(() -> coercer.apply(getString(attribute), 10));
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
