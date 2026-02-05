package object_to_json_parser.test;


import object_to_json_parser.object_parser.ObjectToJsonService;


import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class Runner {
    public static void main(String[] args) {
        City city = new City("Воронеж", Country.RUSSIA);
        Address address = new Address(city);
        Person person = new Person("Алиса", 23, address);
        Class<?> personClass = person.getClass();
        System.out.println(personClass.getSuperclass());

        person.setCountry(Country.USA);
//        String json = ObjectToJsonService.objectToJson(person);
//        System.out.println(json);

        City cityTwo = new City("Москва", Country.RUSSIA);
        City cityThree = new City("Магадан", Country.RUSSIA);
        City[] cities = {city, cityTwo, cityThree};
        String dima = """
                String like\\
                bus stop
                    take a "look"
                """;
        Employee employee = new Employee(dima, 32, address, "Иванов",cities);
        Set hashset = new HashSet<>();
        hashset.add(3);
        hashset.add(2);
        hashset.add(3);
        hashset.add(1);
        employee.setHashSet(hashset);
        Map<Integer,Employee> numbers = new HashMap<>();
        numbers.put(1,new Employee("Алиса", 31, address, "Чудесовна",cities));
        numbers.put(2,new Employee("Никита", 40, address, "Балов",cities));
        numbers.put(3,new Employee("Дима", 26, address, "Федоров",cities));
        employee.setNumbers(numbers);
        employee.setMiddleName("Петрович");
        String jsonEmployee = ObjectToJsonService.objectToJson(employee);
        System.out.println(jsonEmployee);
        System.out.println(String.format("%04x",10));

        Class<? extends Employee> aClass = employee.getClass();
        try {
            Method[] methods = aClass.getMethods();
            Method setter = aClass.getMethod("setName", String.class);
            setter.invoke(employee, "Дима");
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
//        String string = "String like\nbus stop\n\ttake a look;\n";
//        System.out.println(string);
////        System.out.println(employee.getName());
////        System.out.println(aClass.getSuperclass().getName());
//        Box box = new Box("box", 23.0, 123L, Map.of("1", List.of("one", "two")));
//        String json = ObjectToJsonService.objectToJson(box);
//        System.out.println(json);


    }

    public static class Box {
        private String name;
        private double count;
        private Long size;
        private Map<String, List<String>> mapOfLists;

        public Box(String name, double count, Long size, Map<String, List<String>> mapOfLists) {
            this.name = name;
            this.count = count;
            this.size = size;
            this.mapOfLists = mapOfLists;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getCount() {
            return count;
        }

        public void setCount(double count) {
            this.count = count;
        }

        public Long getSize() {
            return size;
        }

        public void setSize(Long size) {
            this.size = size;
        }

        public Map<String, List<String>> getMapOfLists() {
            return mapOfLists;
        }

        public void setMapOfLists(Map<String, List<String>> mapOfLists) {
            this.mapOfLists = mapOfLists;
        }

        @Override
        public String toString() {
            return "Box{" +
                    "name='" + name + '\'' +
                    ", count=" + count +
                    ", size=" + size +
                    ", mapOfLists=" + mapOfLists +
                    '}';
        }
    }
}
