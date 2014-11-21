package hex.repo.metadata;

import java.lang.reflect.Method;
import java.util.function.Function;

/**
 * Created by jason on 14-11-01.
 */
public class CorruptedMetadata<T> extends Metadata<T> {
    private Exception cause;

    public CorruptedMetadata(Exception e) {
        super(null);
        this.cause = e;
    }

    @Override
    public Method getSetter(String fieldName) throws NoSuchMethodException {
        throw new CorruptedMetadataException(cause);
    }

    @Override
    public T newInstance() {
        return super.newInstance();
    }

    @Override
    public void registerSetter(String fieldName, String setterName, Class<?> fieldType) {
        super.registerSetter(fieldName, setterName, fieldType);
    }

    @Override
    public void registerField(Object key, String fieldName) {
        super.registerField(key, fieldName);
    }

    @Override
    public String getFieldName(Function<Object, Object> propertyRef) {
        return super.getFieldName(propertyRef);
    }
}
