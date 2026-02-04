package object_to_json_parser.test;


public class Address {
    private City city;

    public Address() {
    }

    public Address(City city) {
        this.city = city;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }
}
