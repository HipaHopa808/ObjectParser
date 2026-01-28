package object_to_json_parser.object_parser;

public interface Parser<T>{

    ObjectStatement parse(T parseableObj);

}
