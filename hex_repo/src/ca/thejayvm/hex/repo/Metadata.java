package ca.thejayvm.hex.repo;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import static ca.thejayvm.hex.repo.utils.Inflection.*;

/**
 * Created by jason on 14-10-29.
 */
public class Metadata<T> extends ca.thejayvm.jill.sql.Metadata {
    public static <T> Metadata<T> fromClass(Class<T> keyClass) {
        try {
            T keyRecord = keyClass.newInstance();
            Metadata<T> metadata = new Metadata<>(keyRecord);

            for(Field f : keyClass.getDeclaredFields()) {
                f.setAccessible(true);
                f.set(keyRecord, metadata.setFieldMeta(f));
            }

            return metadata;
        } catch (InstantiationException|IllegalAccessException|UnhandledFieldTypeException e) {
            return new CorruptedMetadata<>(e);
        }
    }

    public Metadata(T keyRecord) {
        super(keyRecord);
    }

    public Method getSetter(String fieldName) throws NoSuchMethodException {
        return getKeyRecordClass().getMethod(this.setters.get(fieldName), this.fieldTypes.get(fieldName));
    }

    public T mapRecord(ResultSetWrapper rs) throws InstantiationException, IllegalAccessException, SQLException, NoSuchMethodException, InvocationTargetException {
        T record = newInstance();
        ResultSetMetaData metaData = rs.getMetaData();
        for(int i = 1; i <= metaData.getColumnCount(); i++) {
            Method setter = getSetter(metaData.getColumnLabel(i));
            setter.invoke(record, rs.getValue(i));
        }
        return record;
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

    public Object setFieldMeta(Field f) {
        String fieldName = f.getName();
        String columnName = toColumn(fieldName);
        Object keyValue = generateKey(f);
        registerField(keyValue, columnName);
        registerSetter(columnName, toSetter(fieldName), f.getType());
        return keyValue;
    }

    private static final String GETTER_PREFIX = "get";
    public static String toGetter(String fieldName) {
        return GETTER_PREFIX + capitalize(fieldName);
    }

    private static final String SETTER_PREFIX = "set";
    public static String toSetter(String fieldName) {
        return SETTER_PREFIX + capitalize(fieldName);
    }

    public static String toColumn(String fieldName) {
        return underscore(fieldName);
    }

    private Object generateKey(Field f) {
        switch(f.getType().getName()) {
            case "long":
            case "java.lang.Long":
                return generateLongKey();
            case "int":
            case "java.lang.Integer":
                return generateIntKey();
            case "float":
            case "java.lang.Float":
                return generateFloatKey();
            case "java.lang.String": return toColumn(f.getName());
        }

        throw new UnhandledFieldTypeException("Unhandled field type in generateKey()");
    }

    private int lastIntKey = 0;
    private int generateIntKey() {
        return ++lastIntKey;
    }

    private long lastLongKey = 0;
    private long generateLongKey() {
        return ++lastLongKey;
    }

    private float lastFloatKey = 0.0f;
    private float generateFloatKey() {
        return ++lastFloatKey;
    }
}

class UnhandledFieldTypeException extends RuntimeException {
    public UnhandledFieldTypeException(String message) {
        super(message);
    }
}