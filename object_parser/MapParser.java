package object_to_json_parser.object_parser;

import java.util.Map;
import java.util.Set;

import static object_to_json_parser.object_parser.ObjectToJsonService.*;

public class MapParser implements JsonParser{

    @Override
    public void parseToJson(Object obj,StringBuilder string, int depth) {
        //Счетчик для проставления запятых
        int count = 0;
        string.append("{");
        Map map = (Map) obj;
        Set keys = map.keySet();
        for (Object key : keys) {
            Object value = map.get(key);
            //Формируем поле в формате JSON
            addTabs(string,depth,false);
            addJsonField(string,key);
            //Формируем значение в формате JSON
            JsonParser parser = getParser(value);
            if (parser instanceof ValueParser) {
                parser.parseToJson(value, string, depth);
            } else {
                parser.parseToJson(value, string, depth + 1);
            }
            if (count != map.size() - 1) {
                string.append(",");
                count++;
            }
        }

        addTabs(string,depth,true);
        string.append("}");


    }
}
