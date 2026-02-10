package object_to_json_parser.obj_to_json_service.object_builder;

public interface Builder {
    Object build(CurrentIndex index, Object temp_link, String json, Class<?> objectClass);
}
