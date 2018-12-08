package org.smileframework.tool.string.parsing;

import java.util.Properties;

/**
 * @author liuxin
 * @version Id: PropertyParser.java, v 0.1 2018/11/14 3:35 PM
 */
public class PropertyParser {
    public static String parse(String string, Properties variables) {
        VariableTokenHandler handler = new VariableTokenHandler(variables);
        GenericTokenParser parser = new GenericTokenParser("${", "}", handler);
        return parser.parse(string);
    }

    private static class VariableTokenHandler implements TokenHandler {
        private Properties variables;

        public VariableTokenHandler(Properties variables) {
            this.variables = variables;
        }

        public String handleToken(String content) {
            if (variables != null && variables.containsKey(content)) {
                return variables.getProperty(content);
            }
            return "${" + content + "}";
        }
    }

    public static void main(String[] args) {
        String text = "你好,${liuxin}";
        Properties properties = new Properties();
        properties.put("liuxin","刘鑫");
        String parse = PropertyParser.parse(text, properties);
        System.out.println(parse);

    }
}
