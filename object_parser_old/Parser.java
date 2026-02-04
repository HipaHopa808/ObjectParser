package object_to_json_parser.object_parser_old;

public interface Parser<T>{

    ObjectStatement parse(T parseableObj);

}
