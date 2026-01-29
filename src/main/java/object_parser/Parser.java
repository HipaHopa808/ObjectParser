package object_parser;

public interface Parser<T>{

    ObjectStatement parse(T parseableObj);

}
