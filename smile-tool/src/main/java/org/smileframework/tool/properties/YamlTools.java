package org.smileframework.tool.properties;


import org.json.JSONObject;
import org.yaml.snakeyaml.Yaml;

import java.util.Map;

/**
 * @Package: org.smileframework.tool.properties
 * @Description: yaml文件操作
 * @author: liuxin
 * @date: 2017/12/19 上午11:40
 */
public class YamlTools {
    public static String readJsonAndCreateYaml(String json) {
        Yaml yaml = new Yaml();
        return yaml.dump(json);
    }

    public static Map readYamlToMap(String yamlContent) {
        Yaml yaml = new Yaml();
        return (Map) yaml.load(yamlContent);
    }

    public static void main(String[] args) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("a", "a");
        jsonObject.put("a.b", "b");
        System.out.println(jsonObject.toString());
        System.out.println(readJsonAndCreateYaml(jsonObject.toString()));

        String yamlContent = "redis:\n" +
                "  info:\n" +
                "    ip: 192.168.1.11\n" +
                "    port: 6379\n" +
                "    select: 1\n" +
                "    maxIdle: 24\n" +
                "    Maxtotal: 48\n" +
                "    idleTimeMillis: 10000\n" +
                "    minIdle: 4\n" +
                "    maxWailMills: 30\n" +
                "    onBorrow: true\n" +
                "    onReturn: true";
        //yaml转map
        Map map = readYamlToMap(yamlContent);

    }

}