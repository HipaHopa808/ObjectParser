package object_to_json_parser.object_parser;
import java.util.Collection;
import java.util.Map;

/**
 * Класс предназначен для парсинга экземпляров существующих классов Java в форматированную JSON-строку,
 * и преобразования форматированной JSON-строки в экземпляр существующего класса Java
 *
 */
public final class ObjectToJsonService {
    public static final String LOG_PREFIX = "LOG(object-parser): ";
    /**
     * Метод возвращает JSON-строку, отражающую состояние экземпляра существующего класса
     * на момент его передачи в данный метод
     *
     * @param obj экземпляр существующего класса
     * @return JSON-строка, отражающая состояние экземпляра существующего класса
     */
    public static String objectToJson(Object obj) {
        StringBuilder objectJson = new StringBuilder();
        JsonParser parser = getParser(obj);
        parser.parseToJson(obj,objectJson,0);
        String json = objectJson.toString();
        if(json.equals("")){
            return null;
        }
        return json;
    }

    public static JsonParser getParser(Object fieldValue){
        if(fieldValue == null){
            return new ValueParser();
        } else {
            Class<?> fieldType = fieldValue.getClass();
            if (fieldValue instanceof String
                    || fieldValue instanceof Number
                    || fieldType.isEnum()
                    || fieldValue instanceof Boolean
            ) {
                return new ValueParser();
            } else if (fieldType.isArray()) {
                System.out.println(LOG_PREFIX + "Здесь массив");
                return new ArrayParser();
                //Обработка коллекций
            } else if (fieldValue instanceof Collection) {
                System.out.println(LOG_PREFIX + "Здесь коллекция");
                return new CollectionParser();
                //Обработка словарей
            } else if (fieldValue instanceof Map) {
                System.out.println(LOG_PREFIX + "Здесь словарь");
                return new MapParser();
            } else {
                System.out.println(LOG_PREFIX + "Здесь объект");
                return new ObjectParser();
            }
        }

    }

    /**
     * Метод возвращает ссылку на экземпляр существующего класса, созданный по JSON-строке, отражающей состояние экземпляра существующего класса
     * на момент его передачи в метод ObjectToJson
     *
     * @param json JSON-строка, отражающая состояние экземпляра существующего класса
     * @return ссылка на экземпляр существующего класса, созданный по JSON-строке
     */
    public static Object JsonToObject(String json) {
        Object jsonObject = new Object();
        //Логика создания экземпляра существующего класса из JSON-строки

        return jsonObject;
    }

    protected static void addJsonField(StringBuilder string,Object fieldName){
        string.append("\"");
        string.append(fieldName);
        string.append("\": ");
    }

    protected static void addTabs(StringBuilder string, int depth, boolean isClosed){
        string.append("\n");
        if(isClosed){
            //Выравниваем закрывающую скобку относительно открывающей
            int repeatCount = 0;
            if (depth != 0) {
                repeatCount = depth - 1;
            }
            string.append("  ".repeat(repeatCount));
        } else {
            string.append("  ".repeat(depth));
        }

    }





}
