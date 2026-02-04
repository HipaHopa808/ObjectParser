package object_to_json_parser.object_parser_old;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ObjectBuilder implements Builder<Object>{

    @Override
    public Object build(ObjectStatement objectStatement){
        return null;
    }



    private Map<String, Method> getSetters(Class<?> objectClass){
        Map<String,Method> setters = new HashMap<>();

        return setters;
    }
}
