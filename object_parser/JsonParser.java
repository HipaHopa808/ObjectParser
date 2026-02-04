package object_to_json_parser.object_parser;

public interface JsonParser{

    void parseToJson(Object obj,StringBuilder string,int depth);
}
