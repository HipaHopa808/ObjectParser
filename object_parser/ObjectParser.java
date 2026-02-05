package object_to_json_parser.object_parser;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

import static object_to_json_parser.object_parser.ObjectToJsonService.*;

public class ObjectParser implements JsonParser {

    @Override
    public void parseToJson(Object obj, StringBuilder string, int depth) {
        Class<?> objectClass = obj.getClass();

        //Получаем все поля (с учетом наследования)
        List<Field> declaredFields = new ArrayList<>();
        declaredFields = getAllFields(objectClass, declaredFields);
        List<Method> declaredMethods = new ArrayList<>();
        declaredMethods = getAllMethods(objectClass, declaredMethods);

        String fieldName = null;
        Object fieldValue;
        String setterName;
        boolean isHaveSetter = false;
        //Счетчик для проставления запятых
        int count = 0;


        if(declaredFields.size() == 0){
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
                if(!isHaveSetter){
                    break;
                }
            }

            if (isHaveSetter) {
                string.append("{");
                for (Field declaredField : declaredFields) {
                    declaredField.setAccessible(true);
                    try {
                        fieldValue = declaredField.get(obj);
                        fieldName = declaredField.getName();
                        //Добавляем отступ для первых полей
                        if (count == 0 && depth == 0) {
                            depth = 1;
                        }
                        //Формируем поле в формате JSON
                        addTabs(string,depth,false);
                        addJsonField(string,fieldName);
                        //Формируем значение в формате JSON
                        JsonParser parser = getParser(fieldValue);
                        if (parser instanceof ValueParser) {
                            parser.parseToJson(fieldValue, string, depth);
                        } else {
                            parser.parseToJson(fieldValue, string, depth + 1);
                        }


                        System.out.println(LOG_PREFIX + "Элемент успешно добавлен " + fieldName + ": " + fieldValue);
                        //добавляем запятые между элементами
                        if (count != declaredFields.size() - 1) {
                            string.append(",");
                            count++;
                        }


                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                //Выравниваем закрывающую скобку относительно открывающей
                int repeatCount = depth;
                if (depth != 0) {
                    repeatCount = depth - 1;
                }

                addTabs(string,depth,true);
                string.append("}");

            } else {
                System.out.println(LOG_PREFIX + "Обнаружено поле " + fieldName + " без сэттера. Все поля должны иметь сэттеры! Обработка завершена.");
            }
        }


    }

    //Метод формирует имя сэттера
    private String getSetterName(String fieldName) {
        String fieldNameFirstLetter = fieldName.charAt(0) + "";
        String setterName = "set" + fieldName.replaceFirst(fieldNameFirstLetter, fieldNameFirstLetter.toUpperCase());
        return setterName;
    }

    private List<Method> getAllMethods(Class<?> objectClass, List<Method> declaredMethods) {
        declaredMethods.addAll(Arrays.asList(objectClass.getDeclaredMethods()));
        if (!objectClass.equals(Object.class)) {
            getAllMethods(objectClass.getSuperclass(), declaredMethods);
        }
        return declaredMethods;
    }

    private List<Field> getAllFields(Class<?> objectClass, List<Field> declaredFields) {
        declaredFields.addAll(Arrays.asList(objectClass.getDeclaredFields()));
        if (!objectClass.equals(Object.class)) {
            getAllFields(objectClass.getSuperclass(), declaredFields);
        }
        return declaredFields;

    }


}
