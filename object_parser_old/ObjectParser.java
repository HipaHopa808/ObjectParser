package object_to_json_parser.object_parser_old;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Класс сохраняет состояние объекта в ObjectStatement и возвращает экземпляр класса ObjectStatement
 */
public class ObjectParser implements Parser<Object> {
    private ObjectStatement objectStatement;
    private Class<?> objectClass;

    @Override
    public ObjectStatement parse(Object parseableObj) {
        this.objectClass = parseableObj.getClass();
        this.objectStatement = new ObjectStatement();
        Map<String, Object> fields = new HashMap<>();
        Map<String, Method> setters = new HashMap<>();
        Field[] declaredFields = objectClass.getDeclaredFields();
        Method[] declaredMethods = objectClass.getDeclaredMethods();
        for (Field declaredField : declaredFields) {
            for (Method declaredMethod : declaredMethods) {
                declaredField.setAccessible(true);
                try {

                    String fieldName = declaredField.getName();
                    Object fieldValue = declaredField.get(parseableObj);
                    Class<?> fieldType = declaredField.getType();
                    String fieldNameFirstLetter = fieldName.charAt(0) + "";
                    String setterName = "set" + fieldName.replace(fieldNameFirstLetter, fieldNameFirstLetter.toUpperCase());
                    //проверяем, есть и у поля setter, иначе не сохраняем поле в object.statement
                    if (declaredMethod.getName().equals(setterName)) {
                        //Обработка стандартных значений - строковый тип, число, enum или null
                        if (fieldValue instanceof String
                                || fieldValue instanceof Number
                                || fieldType.isEnum()
                                || fieldValue == null
                        ) {
                            fields.put(fieldName, fieldValue);
                            System.out.println("LOG:Элемент успешно добавлен " + fieldName + ":" + fieldValue);
                            //Обработка массивов
                        } else if (fieldType.isArray()) {
                            List list = parseArray(fieldValue);
                            fields.put(fieldName, fieldValue);
                            System.out.println("LOG:Здесь массив");
                        } else if (fieldValue instanceof Collection) {
                            System.out.println("LOG:Здесь коллекция");
                        } else if (fieldValue instanceof Map) {
                            System.out.println("LOG:Здесь словарь");
                        } else {
                            Object objectValue = parse(fieldValue);
                            fields.put(fieldName, objectValue);

                            System.out.println("LOG:Здесь объект");
                        }
                        setters.put(fieldName, declaredMethod);

                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        this.objectStatement.setFields(fields);
        this.objectStatement.setSetters(setters);

        return getObjectStatement();
    }

    public ObjectStatement getObjectStatement() {
        if (this.objectStatement == null) {
            return null;
        }
        ObjectStatement objectStatement = new ObjectStatement();
        objectStatement.setFields(this.objectStatement.getFields());
        objectStatement.setSetters(this.objectStatement.getSetters());

        return objectStatement;
    }

    private List parseArray(Object fieldValue) {
        List array = new ArrayList<>();
        int length = Array.getLength(fieldValue);
        for (int i = 0; i < length; i++) {
            Object element = Array.get(fieldValue, i);
            Class<?> elementType = element.getClass();
            if (element instanceof String
                    || element instanceof Number
                    || elementType.isEnum()
                    || element == null
            ) {
                array.add(element);
            } else if (elementType.isArray()) {
                List list = parseArray(element);
                array.add(list);
                System.out.println("LOG:Здесь массив");
            } else if (fieldValue instanceof Collection) {
                System.out.println("LOG:Здесь коллекция");
            } else if (fieldValue instanceof Map) {
                System.out.println("LOG:Здесь словарь");
            } else {
                Object objectValue = parse(element);
                array.add(objectValue);

                System.out.println("LOG:Здесь объект");
            }

        }
        return array;
    }


}
