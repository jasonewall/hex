package hex.repo.metadata;

import hex.ql.ast.Node;
import hex.ql.ast.Variable;
import hex.repo.RepositoryException;
import hex.repo.ResultSetWrapper;
import hex.repo.streams.RepositoryStream;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import static hex.utils.Inflection.*;

/**
 * Created by jason on 14-10-29.
 */
public class Metadata<T> extends hex.repo.sql.Metadata {
    private static final int FAILED_PK_RETRIEVAL = -1;

    public static <T> Metadata<T> fromClass(Class<T> keyClass) {
        try {
            T keyRecord = keyClass.newInstance();
            Metadata<T> metadata = new Metadata<>(keyRecord);

            for(Field f : keyClass.getDeclaredFields()) {
                f.setAccessible(true);
                f.set(keyRecord, metadata.setFieldMeta(f));
            }

            Method pk_getter = keyClass.getMethod(toGetter(ID));

            metadata.primaryKey = (t) -> {
                try {
                    return (Integer)pk_getter.invoke(t);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    return FAILED_PK_RETRIEVAL;
                }
            };

            metadata.tableName = tableize(keyClass.getSimpleName());

            return metadata;
        } catch (InstantiationException|IllegalAccessException|UnhandledFieldTypeException|NoSuchMethodException e) {
            return new CorruptedMetadata<>(e);
        }
    }

    private static final String ID = "id";

    private Function<T,Integer> primaryKey;

    public Metadata(T keyRecord) {
        super(keyRecord);
    }

    public Function<T,Integer> getPrimaryKey() {
        return primaryKey;
    }

    public Method getSetter(String fieldName) throws NoSuchMethodException {
        return getKeyRecordClass().getMethod(this.setters.get(fieldName), this.fieldTypes.get(fieldName));
    }

    public T mapRecord(ResultSetWrapper rs) throws DataMappingException {
        try {
            T record = newInstance();
            ResultSetMetaData metaData = rs.getMetaData();
            for(int i = 1; i <= metaData.getColumnCount(); i++) {
                Method setter = getSetter(metaData.getColumnLabel(i));
                setter.invoke(record, rs.getValue(i));
            }
            return record;
        } catch (IllegalAccessException | SQLException | NoSuchMethodException | InvocationTargetException e) {
            throw new DataMappingException(e);
        }
    }

    @SuppressWarnings("unchecked")
    protected Class<T> getKeyRecordClass() {
        T t = (T)super.getKeyRecord();
        return (Class<T>)t.getClass();
    }

    public Node[] getColumns() {
        return fieldTypes.keySet().stream().map(Variable::new).toArray(Node[]::new);
    }

    private String tableName;

    public String getTableName() {
        return tableName;
    }

    public T newInstance() {
        try {
            return this.getKeyRecordClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RepositoryException(e);
        }
    }

    private Map<String,String> setters = new HashMap<>();
    private Map<String,Class> fieldTypes = new HashMap<>();

    public void registerSetter(String fieldName, String setterName, Class<?> fieldType) {
        this.setters.put(fieldName, setterName);
        this.fieldTypes.put(fieldName, fieldType);
    }

    private T invocationTracker() {
        return newInstance();
    }

    public Object setFieldMeta(Field f) throws UnhandledFieldTypeException {
        String fieldName = f.getName();
        String columnName = toColumn(fieldName);
        Object keyValue = generateKey(f);
        registerField(keyValue, columnName);
        registerSetter(columnName, toSetter(fieldName), f.getType());
        return keyValue;
    }

    public static String toColumn(String fieldName) {
        return underscore(fieldName);
    }

    private Object generateKey(Field f) throws UnhandledFieldTypeException {
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

    public <R> RepositoryStream<R> mapToRepositoryStream(Function<? super T, ? extends Stream<? extends R>> mapper) {
        T o = invocationTracker();
        Stream<? extends R> childStream = mapper.apply(o);
        if(childStream instanceof RepositoryStream) {
            @SuppressWarnings("unchecked")
            RepositoryStream<R> childRepo = (RepositoryStream<R>)childStream;
            return childRepo;
        }

        throw new RepositoryException(new IllegalStateException("flatMap did not result in repository stream"));
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

