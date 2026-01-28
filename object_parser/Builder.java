package object_to_json_parser.object_parser;

public interface Builder<T>{
    T build(ObjectStatement buildableObj);
}
