package object_to_json_parser.test;

public enum Country {
    RUSSIA("Россия"),
    USA("CША");
    private String name;

    Country(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
