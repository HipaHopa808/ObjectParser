package object_to_json_parser.obj_to_json_service.object_builder;

import static object_to_json_parser.obj_to_json_service.ObjectToJsonService.LOG_PREFIX;

public class ValueBuilder implements Builder {
    //1.Добавить обработку escape-последовательностей в обработчик строки
    // 2.Добавить обработку ошибок с выводом сообщений, программа не должна завершаться аварийно,
    // а выводить сообщения в лог и возвращать null
    // 2.Отрефакторить код, произвести декомпозицию существующего метода
    // для более удобной поддержки
    @Override
    public Object build(CurrentIndex index, Object temp_link, String json, Class<?> objectClass) {

        char[] jsonCharArray = json.toCharArray();
        Object value = null;
        char letter;
        boolean inValue = false;
        boolean inString = false;
        boolean inDigit = false;
        boolean isDouble = false;
        boolean searchComma = false;
        int quotesCount = 0;
        StringBuilder stringValue = new StringBuilder();
        StringBuilder pattern = new StringBuilder();

        for (int i = index.getIndex(); i < jsonCharArray.length; i++) {
            letter = jsonCharArray[i];
            index.setIndex(i);
            if (letter == ':' && !(inValue || inString || inDigit || searchComma)) {
                inValue = true;
            }

            if (inValue) {
                if (objectClass == String.class) {
                    if (inString) {
                        if (letter == '"' && quotesCount == 1) {
                            quotesCount = 2;
                            value = stringValue.toString();
                            inString = false;
                            searchComma = true;
                            continue;
                        }
                        stringValue.append(letter);
                    }
                    if (letter == '"' && quotesCount == 0) {
                        inString = true;
                        quotesCount = 1;
                    }


                } else if (objectClass.getSuperclass() == Number.class) {
                    if (Character.isDigit(letter) || letter == '-') {
                        if (letter == '-') {
                            stringValue.append(letter);
                        }
                        inDigit = true;
                    }
                    if (inDigit) {
                        Number parsedNumber;
                        if (Character.isDigit(letter)) {
                            stringValue.append(letter);
                        } else if (letter == '.' && !isDouble) {
                            isDouble = true;
                            stringValue.append('.');
                        } else {
                            if (isDouble) {
                                parsedNumber = Double.valueOf(stringValue.toString());
                            } else {
                                parsedNumber = Long.valueOf(stringValue.toString());
                            }
                            Double doubleParsedNumber = parsedNumber.doubleValue();
                            if (doubleParsedNumber.equals(Math.floor(doubleParsedNumber))) {
                                if (objectClass == Integer.class) {
                                    value = parsedNumber.intValue();
                                } else if (objectClass == Long.class) {
                                    value = parsedNumber.longValue();
                                } else if (objectClass == Short.class) {
                                    value = parsedNumber.shortValue();
                                } else if (objectClass == Long.class) {
                                    value = parsedNumber.byteValue();
                                }
                            } else if (objectClass == Integer.class
                                    || objectClass == Long.class
                                    || objectClass == Byte.class
                                    || objectClass == Short.class) {
                                System.out.println(LOG_PREFIX + "Ошибка приведения типов: " + parsedNumber.doubleValue()
                                        + " не может быть приведен к целочисленному типу без потери точности.");
                            } else {
                                if (objectClass == Double.class) {
                                    value = parsedNumber.doubleValue();
                                } else if (objectClass == Float.class) {
                                    value = parsedNumber.floatValue();
                                }
                            }
                            inDigit = false;
                            inValue = false;
                            searchComma = true;
                        }
                    }

                } else if (objectClass.isEnum()) {

                } else if (objectClass == Boolean.class) {
                    if(Character.isLetter(letter)){
                        pattern.append(letter);
                    }

                    if (pattern.toString().contains("true")) {
                        value = Boolean.TRUE;
                        inValue = false;
                        searchComma = true;
                    } else if (pattern.toString().contains("false")) {
                        value = Boolean.FALSE;
                        inValue = false;
                        searchComma = true;
                    }


                }
            }

            if (searchComma) {
                if (letter == ',' || letter == '}') {
                    break;
                }
            }


        }


        return value;
    }

    private static Object getFieldValue(CurrentIndex index, String json) {
        StringBuilder stringValue = new StringBuilder();
        StringBuilder pattern = new StringBuilder();
        Object value = null;
        char letter;
        boolean inValue = false;
        boolean inString = false;
        boolean inDigit = false;
        boolean isDouble = false;
        boolean searchComma = false;

        char[] jsonCharArray = json.toCharArray();

        for (int i = index.getIndex(); i < jsonCharArray.length; i++) {
            letter = jsonCharArray[i];
            index.setIndex(i);
            if (searchComma) {
                if (letter == ',' || letter == '}') {
                    break;
                }
            }
            if (letter == ':' && !(inValue || inString || inDigit || searchComma)) {
                inValue = true;
            }

            if (inValue) {
                pattern.append(letter);
                if (letter == '"') {
                    inString = true;
                    inValue = false;
                } else if (pattern.toString().equals("true")) {
                    value = Boolean.TRUE;
                    inValue = false;
                    searchComma = true;
                } else if (pattern.toString().equals("false")) {
                    value = Boolean.FALSE;
                    inValue = false;
                    searchComma = true;
                } else if (pattern.toString().equals("null")) {
                    value = null;
                    inValue = false;
                    searchComma = true;
                } else {
                    if (Character.isDigit(letter) || letter == '-') {
                        if (letter == '-') {
                            stringValue.append(letter);
                        }
                        inDigit = true;
                        inValue = false;
                    }
                }

            }
            if (inDigit) {
                Number parsedNumber;
                if (Character.isDigit(letter)) {
                    stringValue.append(letter);
                } else if (letter == '.' && !isDouble) {
                    isDouble = true;
                    stringValue.append('.');
                } else {
                    if (isDouble) {
                        parsedNumber = Double.valueOf(stringValue.toString());
                    } else {
                        parsedNumber = Long.valueOf(stringValue.toString());
                    }
                    value = parsedNumber;
                    inDigit = false;
                    searchComma = true;
                }
            }

            if (inString) {
                if (letter == '"') {
                    value = stringValue.toString();
                    inString = false;
                    searchComma = true;
                }
                stringValue.append(letter);
            }

        }

        return value;
    }
}
