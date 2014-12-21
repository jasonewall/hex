package hex.utils.generics;

import java.lang.reflect.Field;

/**
 * Created by jason on 14-12-20.
 */
public class TypeVariables {
    public static Class<?> collectionTypeOf(Field field) {
        CollectionType type = field.getAnnotation(CollectionType.class);
        if(type == null) return Object.class;
        return type.value();
    }
}
