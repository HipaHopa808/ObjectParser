package object_to_json_parser.object_parser;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

public class ArrayParser implements JsonParser{

    @Override
    public void parseToJson(Object obj,StringBuilder string,int depth) {
        string.append("[");
        int length = Array.getLength(obj);
        for (int i = 0; i < length; i++) {
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

        string.append("]");

    }
}
