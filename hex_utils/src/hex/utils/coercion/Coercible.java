package hex.utils.coercion;

/**
 * Created by jason on 14-12-14.
 */
public interface Coercible {
    public <T> T coerce(Class<T> intoType) throws CoercionException;

    default public <T> T newInstance(Class<T> type) throws CoercionException {
        try {
            return type.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new CoercionException(e);
        }
    }
}
