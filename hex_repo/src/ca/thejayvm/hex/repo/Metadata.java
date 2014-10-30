package ca.thejayvm.hex.repo;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jason on 14-10-29.
 */
public class Metadata<T> extends ca.thejayvm.jill.sql.Metadata {
    public Metadata(T keyRecord) {
        super(keyRecord);
    }

    public Method getSetter(String fieldName) throws NoSuchMethodException {
        return getKeyRecordClass().getMethod(this.setters.get(fieldName), this.fieldTypes.get(fieldName));
    }

    @SuppressWarnings("unchecked")
    protected Class<T> getKeyRecordClass() {
        T t = (T)super.getKeyRecord();
        return (Class<T>)t.getClass();
    }

    public T newInstance() throws IllegalAccessException, InstantiationException {
        return this.getKeyRecordClass().newInstance();
    }

    private Map<String,String> setters = new HashMap<>();
    private Map<String,Class> fieldTypes = new HashMap<>();

    public void registerSetter(String fieldName, String setterName, Class<?> fieldType) {
        this.setters.put(fieldName, setterName);
        this.fieldTypes.put(fieldName, fieldType);
    }
}
