package object_to_json_parser.obj_to_json_service.object_parser;

import object_to_json_parser.obj_to_json_service.ObjectToJsonService;

import java.lang.reflect.Array;

import static object_to_json_parser.obj_to_json_service.ObjectToJsonService.addTabs;

public class ArrayParser implements JsonParser{

    @Override
    public void parseToJson(Object obj,StringBuilder string,int depth) {
        string.append("[");
        int length = Array.getLength(obj);
        for (int i = 0; i < length; i++) {
            addTabs(string,depth,false);
            Object element = Array.get(obj, i);
            JsonParser parser = ObjectToJsonService.getParser(element);
            if (parser instanceof ValueParser){
                parser.parseToJson(element,string,depth);
            } else {
                parser.parseToJson(element,string,depth + 1);
            }
            if (i != length - 1) {
                string.append(",");
            }
        }

        addTabs(string,depth,true);
        string.append("]");

    }
}
