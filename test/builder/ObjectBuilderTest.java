package object_to_json_parser.test.builder;

import object_to_json_parser.obj_to_json_service.ObjectToJsonService;

public class ObjectBuilderTest {
    public static void main(String[] args) {
        Person person = new Person();
        person.setAge(32L);
        person.setName("Alice:");
        String json = ObjectToJsonService.objectToJson(person);
        json = """
                {
                    "name":"Alice:alice",
                    "age":df32,
                    "isWorker": false
                }""";
        System.out.println(json);
        String string = "true";
        boolean bool = Boolean.TRUE;


       Person personOne = (Person) ObjectToJsonService.JsonToObject(json, Person.class);
       System.out.println(personOne.toString());



    }
}
