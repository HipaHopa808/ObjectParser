package object_to_json_parser.test.builder;

import object_to_json_parser.obj_to_json_service.ObjectToJsonService;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ObjectBuilderTest {
    public static void main(String[] args) {
        Person person = new Person();
        person.setAge(32);
        person.setName(
                """
                Hello,
                        my litte"""
        );
        person.setWorking(true);
        person.setWeight(67.5F);
        String json = ObjectToJsonService.objectToJson(person);
        json = """
                {
                    "name":"Alice:\\n alice",
                    "age":df32123d44L,
                    "working": falsetrue
                }""";
        System.out.println(json);
        String string = "true";
        boolean bool = Boolean.TRUE;


       Person personOne = (Person) ObjectToJsonService.JsonToObject(json, Person.class);
       System.out.println(personOne.toString());
       System.out.println(personOne.getName());

////        try {
////            writeStringToFile(json, "output.txt");
////        } catch (IOException e) {
////            System.err.println("Ошибка записи файла: " + e.getMessage());
////        }
//        String content = "";
//
//        try {
//            content = readStringFromFile("output.txt");
//            System.out.println(content);
//        } catch (IOException e) {
//            System.err.println("Ошибка чтения файла: " + e.getMessage());
//        }
//        Person personTwo = (Person) ObjectToJsonService.JsonToObject(content, Person.class);
//        System.out.println(personTwo.toString());
//        System.out.println(personTwo.getName());




    }

    public static void writeStringToFile(String content, String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(content);
        }
    }

    public static String readStringFromFile(String filePath) throws IOException {
        return Files.readString(Paths.get(filePath), StandardCharsets.UTF_8);
    }
}
