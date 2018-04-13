package org.smileframework.tool.properties;


import org.json.JSONObject;
import org.yaml.snakeyaml.Yaml;

import java.util.Map;
import java.util.Random;
import java.util.UUID;

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
        String substring = UUID.randomUUID().toString().replaceAll("-", "");
        System.out.println(substring);
        //appKey: 5341594848
        //appSecret: 1358dda1ec6a41a7b0f0b0f0ec1bc88f
    }

}