package hex.action.params;

import hex.utils.maps.AbstractImmutableMap;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
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
}
