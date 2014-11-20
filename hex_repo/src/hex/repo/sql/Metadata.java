package hex.repo.sql;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Created by jason on 14-10-26.
 */
public class Metadata {
    private Object keyRecord;

    public Metadata(Object keyRecord) {
        this.keyRecord = keyRecord;
    }

    private Map<Object,String> fieldKeys = new HashMap<>();

    public void registerField(Object key, String fieldName) {
        this.fieldKeys.put(key, fieldName);
    }

    public String getFieldName(Function<Object,Object> propertyRef) {
        return fieldKeys.get(propertyRef.apply(keyRecord));
    }

    protected Object getKeyRecord() {
        return keyRecord;
    }
}
