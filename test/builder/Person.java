package object_to_json_parser.test.builder;

public class Person {
    private String name;
    private long age;
    private boolean working;
    private float weight;


    public Person() {
    }

    public Person(String name, long age, boolean working) {
        this.name = name;
        this.age = age;
        this.working = working;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getAge() {
        return age;
    }

    public void setAge(long age) {
        this.age = age;
    }

    public boolean isWorking() {
        return working;
    }

    public void setWorking(boolean working) {
        this.working = working;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", working=" + working +
                ", weight=" + weight +
                '}';
    }
}
