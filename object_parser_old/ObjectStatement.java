package object_to_json_parser.object_parser_old;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ObjectStatement {
    private Map<String, Method> setters;
    private Map<String, Object> fields;

    public Map<String, Method> getSetters() {
        if(this.setters == null){
            return null;
        }
        Map<String, Method> setters = new HashMap<>(this.setters);
        return setters;
    }

    public void setSetters(Map<String, Method> setters) {
        this.setters = setters;
    }

    public Map<String, Object> getFields() {
        if(this.setters == null){
            return null;
        }
        Map<String, Object> fields =  new HashMap<>(this.fields);
        return fields;
    }

    public void setFields(Map<String, Object> fields) {
        this.fields = fields;
    }
}

