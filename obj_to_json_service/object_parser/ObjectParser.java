package object_to_json_parser.obj_to_json_service.object_parser;

import java.lang.reflect.Field;
import java.util.*;

import static object_to_json_parser.obj_to_json_service.ObjectToJsonService.*;

public class ObjectParser implements JsonParser {

    @Override
    public void parseToJson(Object obj, StringBuilder string, int depth) {
        Class<?> objectClass = obj.getClass();

        //Получаем все поля (с учетом наследования)
        List<Field> declaredFields = new ArrayList<>();
        declaredFields = getAllFields(objectClass, declaredFields);

        String fieldName = null;
        Object fieldValue;

        //Счетчик для проставления запятых
        int count = 0;

        if (declaredFields.size() == 0) {
            System.out.println(LOG_PREFIX + "У класса нет полей! Обработка завершена");
        } else {
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
                    addTabs(string, depth, false);
                    addJsonField(string, fieldName);
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

            addTabs(string, depth, true);
            string.append("}");


        }

    }


}
