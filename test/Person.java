package object_to_json_parser.test;


import java.util.HashSet;
import java.util.Set;

public class Person {
    private String name;
    private int age;
    private Address address;
    private Country country;
    private int[][] ids = {{1,2,3},{1,2,3,4}};
    private Set hashSet;



    public Person() {
    }

    public Person(String name, int age, Address address) {
        this.name = name;
        this.age = age;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Address getAddress() {
        return address;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public void setIds(int[][] ids) {
        this.ids = ids;
    }

    public void setHashSet(Set hashSet) {
        this.hashSet = hashSet;
    }
}
