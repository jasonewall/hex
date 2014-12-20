package hex.action.params;

import hex.utils.coercion.CoercionException;
import hex.utils.maps.AbstractImmutableMap;

import java.lang.reflect.Array;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jason on 14-12-14.
 */
public class ParameterMap extends AbstractImmutableMap<String,Object> implements Params {
    protected Map<String,Object> params;

    public ParameterMap(int initialCapacity) {
        this.params = new HashMap<>(initialCapacity);
    }

    @Override
    protected Set<Entry<String, Object>> buildEntries() {
        Map<String,Object> entries = new HashMap<>(params.size());
        params.forEach((k,v) -> {
            Pattern p = Pattern.compile("(\\w+)\\[(\\w+)\\](.*)"); // (\w+)\[(\w+\)](.*)
            Matcher m = p.matcher(k);
            if(m.matches()) {
                if(!entries.containsKey(m.group(1))) {
                    entries.put(m.group(1), new ParameterMap(params.size()));
                }
                ParameterMap subParams = (ParameterMap) entries.get(m.group(1));
                subParams.params.put(m.group(2) + m.group(3), v);
            } else {
                entries.put(k, v);
            }
        });
        return entries.entrySet();
    }

    @Override
    public Object coerceArray(Object array, Class<?> intoType) throws CoercionException {
        if(array.getClass().isArray())
            return Params.super.coerceArray(array, intoType);

        // assuming parameter map for now
        ParameterMap params = (ParameterMap)array;
        return Params.super.coerceArray(params.rotate(), intoType);
    }

    private ParameterMap[] rotate() {
        List<ParameterMap> results = new ArrayList<>();
        entrySet().stream().findFirst().ifPresent(e -> {
            int length = Array.getLength(e.getValue());
            for(int i = 0; i < length; i++) {
                //noinspection MismatchedQueryAndUpdateOfCollection
                ParameterMap params = new ParameterMap(size() + 10);
                params.params.put(e.getKey(), Array.get(e.getValue(), i));
                results.add(params);
            }
        });

        entrySet().stream().skip(1).forEach(e -> {
            int length = Array.getLength(e.getValue());
            for(int i = 0; i < length; i++) {
                results.get(i).params.put(e.getKey(), Array.get(e.getValue(), i));
            }
        });
        return results.stream().toArray(ParameterMap[]::new);
    }
}
