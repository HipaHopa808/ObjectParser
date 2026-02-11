package object_to_json_parser.obj_to_json_service;

import object_to_json_parser.obj_to_json_service.object_builder.*;
import object_to_json_parser.obj_to_json_service.object_parser.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

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
        parser.parseToJson(obj, objectJson, 0);
        String json = objectJson.toString();
        if (json.equals("")) {
            return null;
        }
        return json;
    }

    public static JsonParser getParser(Object fieldValue) {
        if (fieldValue == null) {
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
    public static Object JsonToObject(String json, Class<?> objectClass) {

        Object jsonObject = null;
        try {
            jsonObject = objectClass.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        //Логика создания экземпляра существующего класса из JSON-строки
        Builder builder = new ObjectBuilder();
        jsonObject = builder.build(new CurrentIndex(0), jsonObject, json, objectClass);

        return jsonObject;
    }

    public static Builder getBuilder(Object obj) {
        if (obj == null) {
            return new ValueBuilder();
        } else {
            Class<?> fieldType = obj.getClass();
            if (obj instanceof String
                    || obj instanceof Number
                    || fieldType.isEnum()
                    || obj instanceof Boolean
            ) {
                return new ValueBuilder();
            } else if (fieldType.isArray()) {
                System.out.println(LOG_PREFIX + "Здесь массив");
                return new ArrayBuilder();
                //Обработка коллекций
            } else if (obj instanceof Collection) {
                System.out.println(LOG_PREFIX + "Здесь коллекция");
                return new CollectionBuilder();
                //Обработка словарей
            } else if (obj instanceof Map) {
                System.out.println(LOG_PREFIX + "Здесь словарь");
                return new MapBuilder();
            } else {
                System.out.println(LOG_PREFIX + "Здесь объект");
                return new ObjectBuilder();
            }
        }

    }


//    private static boolean isPattern(CurrentIndex index, String json, String pattern) {
//        boolean isPattern;
//        int innerIndex = 0;
//        StringBuilder checkedString = new StringBuilder();
//        char letter;
//        char[] jsonCharArray = json.toCharArray();
//
//        for (int i = index.getIndex(); i < index.getIndex() + pattern.length(); i++) {
//            innerIndex = i;
//            letter = jsonCharArray[i];
//            checkedString.append(letter);
//        }
//
//        isPattern = pattern.equals(checkedString.toString());
//        if(isPattern){
//            index.setIndex(innerIndex);
//        }
//
//        return isPattern;
//    }


    public static String getFieldName(CurrentIndex index, String json) {
        StringBuilder name = new StringBuilder();
        char letter;
        int inField = 0;
        int innerIndex = 0;

        char[] jsonCharArray = json.toCharArray();


        for (int i = index.getIndex(); i < jsonCharArray.length; i++) {
            letter = jsonCharArray[i];
            innerIndex = i;
            if (letter == ':') {
                break;
            }
            if (inField == 1) {
                if (letter == '"') {
                    inField = 2;
                    continue;
                }
                name.append(letter);
            }
            if (letter == '"' && inField == 0) {
                inField = 1;
            }


        }
        index.setIndex(innerIndex);

        return name.toString();
    }


    public static void addJsonField(StringBuilder string, Object fieldName) {
        string.append("\"");
        string.append(fieldName);
        string.append("\": ");
    }

    public static void addTabs(StringBuilder string, int depth, boolean isClosed) {
        string.append("\n");
        if (isClosed) {
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

    //Метод формирует имя сэттера
    //Добавить обработку имен сэттеров по правилам JavaBean (для булевых значений isName и т.п.)
    public static String getSetterName(String fieldName) {
        if (fieldName.substring(0, 1).equals("is")) {
            fieldName = fieldName.replaceFirst("is","");
        }
        String fieldNameFirstLetter = fieldName.charAt(0) + "";
        String setterName = "set" + fieldName.replaceFirst(fieldNameFirstLetter, fieldNameFirstLetter.toUpperCase());

        return setterName;
    }

    public static boolean isHaveAllSetters(Class<?> objectClass) {
        //Получаем все поля (с учетом наследования)
        List<Field> declaredFields = new ArrayList<>();
        declaredFields = getAllFields(objectClass, declaredFields);
        List<Method> declaredMethods = new ArrayList<>();
        declaredMethods = getAllMethods(objectClass, declaredMethods);

        String fieldName = null;
        Object fieldValue;
        String setterName;
        boolean isHaveSetter = false;

        if (declaredFields.size() == 0) {
            System.out.println(LOG_PREFIX + "У класса нет полей! Обработка завершена");
        } else {
            //Проверка на сэттеры
            for (Field declaredField : declaredFields) {
                isHaveSetter = false;
                fieldName = declaredField.getName();
                for (Method declaredMethod : declaredMethods) {
                    setterName = getSetterName(fieldName);
                    if (declaredMethod.getName().equals(setterName)) {
                        isHaveSetter = true;
                        break;
                    }
                }
                //Если не один метод не совпал с именем сэттера, то выходим из цикла
                if (!isHaveSetter) {
                    System.out.println(LOG_PREFIX + "Обнаружено поле " + fieldName + " без сэттера. Все поля должны иметь сэттеры! Обработка завершена.");
                    break;
                }
            }

        }
        return isHaveSetter;
    }

    public static List<Method> getAllMethods(Class<?> objectClass, List<Method> declaredMethods) {
        declaredMethods.addAll(Arrays.asList(objectClass.getDeclaredMethods()));
        if (!objectClass.equals(Object.class)) {
            getAllMethods(objectClass.getSuperclass(), declaredMethods);
        }
        return declaredMethods;
    }

    public static List<Field> getAllFields(Class<?> objectClass, List<Field> declaredFields) {
        declaredFields.addAll(Arrays.asList(objectClass.getDeclaredFields()));
        if (!objectClass.equals(Object.class)) {
            getAllFields(objectClass.getSuperclass(), declaredFields);
        }
        return declaredFields;

    }


}
