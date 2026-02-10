package object_to_json_parser.obj_to_json_service.object_parser;

public interface JsonParser{

    void parseToJson(Object obj,StringBuilder string,int depth);
}
