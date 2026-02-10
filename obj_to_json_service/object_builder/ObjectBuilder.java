package object_to_json_parser.obj_to_json_service.object_builder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static object_to_json_parser.obj_to_json_service.ObjectToJsonService.*;

public class ObjectBuilder implements Builder{
    private boolean inObject = false;
    //1.Добавить обработку ошибок с выводом сообщений,
    // добавить обработку null значений перед передачей их в сэттер(не все сэттеры могут принимать null!)
    // 2.Отрефакторить код, произвести декомпозицию существующего метода
    // для более удобной поддержки
    @Override
    public Object build(CurrentIndex index, Object temp_link, String json, Class<?> objectClass) {
        char[] jsonCharArray = json.toCharArray();
        for (int i = index.getIndex(); i < jsonCharArray.length;i++) {
            char letter = jsonCharArray[i];
            if(letter == '{') {
                inObject = true;
            }
            if(inObject){
                if(temp_link != null){

                    String fieldName = getFieldName(index,json);
                    String setterName = getSetterName(fieldName);

                    try {
                        Class<?> fieldType = objectClass.getDeclaredField(fieldName).getType();
                        Method setter = objectClass.getMethod(setterName,fieldType);
                        Object newObjectForSetter = null;
                        if(!fieldType.isPrimitive()){
                             newObjectForSetter = fieldType.newInstance();
                        }
                        Builder builder = getBuilder(newObjectForSetter);
                        Object fieldValue = builder.build(index,newObjectForSetter,json,toWrapperType(fieldType));
                        setter.invoke(temp_link,toWrapperType(fieldType).cast(fieldValue));
                        i = index.getIndex();

                    } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                             InvocationTargetException | NoSuchFieldException e) {
                        throw new RuntimeException(e);
                    }

                } else {
                    System.out.println("Необходимо передать ссылку на объект! Обработка завершена");
                    return null;
                }

            }
        }
        if(jsonCharArray[jsonCharArray.length - 1] == '}'){
            return temp_link;
        } else {
            System.out.println("Невалидный json! Отсутствует '}'. Обработка завершена");
            return null;
        }
    }

    private static Class<?> toWrapperType(Class<?> defaultClass){
        if(defaultClass == int.class){
            return Integer.class;
        } else if (defaultClass == long.class) {
            return Long.class;
        } else if (defaultClass == byte.class) {
            return Byte.class;
        } else if (defaultClass == short.class) {
            return Short.class;
        } else if (defaultClass == double.class) {
            return Double.class;
        } else if (defaultClass == float.class) {
            return Float.class;
        } else if (defaultClass == char.class) {
            return Character.class;
        } else if (defaultClass == boolean.class) {
            return Boolean.class;
        } else {
            return defaultClass;
        }
    }

}
