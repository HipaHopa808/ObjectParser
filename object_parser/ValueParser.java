package object_to_json_parser.object_parser;

public class ValueParser implements JsonParser {

    @Override
    public void parseToJson(Object obj, StringBuilder string, int depth) {
        if (obj instanceof Number || obj == null || obj instanceof Boolean) {
            string.append(obj);
        } else {
            if (obj instanceof String) {
                string.append("\"");
                for (char letter : String.class.cast(obj).toCharArray()) {
                    if (letter == '"') {
                        string.append("\\\"");          // " -> \"
                    } else if (letter == '\\') {
                        string.append("\\\\");          // \ -> \\
                    } else if (letter == '\b') {
                        string.append("\\b");           // backspace
                    } else if (letter == '\f') {
                        string.append("\\f");
                    } else if (letter == '\n') {
                        string.append("\\n");           // newline(новая строка)
                    } else if (letter == '\r') {
                        string.append("\\r");           // carriage return (возврат каретки)
                    } else if (letter == '\t') {
                        string.append("\\t");           // tab(отступ)
                    } else if (letter <= 0x1F || (letter >= 0x7F && letter <= 0x9F)) {
                        // Управляющие символы → \\uXXXX
                        string.append(String.format("\\u%04x", (int) letter));
                    } else {
                        // Все остальные символы — оставляем как есть
                        string.append(letter);
                    }
                }
                string.append("\"");
            } else {
                string.append("\"" + obj + "\"");
            }
        }
    }
}