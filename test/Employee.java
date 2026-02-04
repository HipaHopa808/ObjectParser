package object_to_json_parser.test;

public class Employee extends Person{
    private String middleName;
    private City[] cities;

    public Employee() {
    }

    public Employee(String name, int age, Address address, String middleName, City[] cities) {
        super(name, age, address);
        this.middleName = middleName;
        this.cities = cities;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public void setCities(City[] cities) {
        this.cities = cities;
    }
}
