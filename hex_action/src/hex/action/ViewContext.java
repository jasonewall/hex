package hex.action;

import hex.utils.maps.CoercionMap;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jason on 14-11-15.
 */
public class ViewContext extends HashMap<String,Object> implements CoercionMap<String> {
    private static final String BLANK = "";

    private String content;

    private Map<String,String> sections = new HashMap<>();

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setSectionContent(String sectionName, String content) {
        this.sections.put(sectionName, content);
    }

    public String getSectionContent(String sectionName) {
        if(!this.sections.containsKey(sectionName)) return BLANK;
        return this.sections.get(sectionName);
    }
}
