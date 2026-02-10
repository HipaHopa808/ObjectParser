package object_to_json_parser.obj_to_json_service.object_parser;

import java.util.Collection;

public class CollectionParser implements JsonParser {
    @Override
    public void parseToJson(Object obj, StringBuilder string,int depth) {
        Collection collection = (Collection)obj;
        Object array = collection.toArray();
        ArrayParser arrayParser = new ArrayParser();
        arrayParser.parseToJson(array,string,depth);
    }
}
