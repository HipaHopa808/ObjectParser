package object_to_json_parser.test.builder;

public class Person {
    private String name;
    private long age;
    private boolean isWorker;


    public Person() {
    }

    public Person(String name, long age, boolean isWorker) {
        this.name = name;
        this.age = age;
        this.isWorker = isWorker;
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

    public boolean isWorker() {
        return isWorker;
    }

    public void setIsWorker(boolean worker) {
        isWorker = worker;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", isWorker=" + isWorker +
                '}';
    }
}
