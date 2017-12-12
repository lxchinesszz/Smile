package org.smileframework.tool.clazz;

import org.smileframework.tool.json.JsonUtils;

/**
 * @Package: org.smileframework.tool.clazz
 * @Description:提供强制转换
 * @author: liuxin
 * @date: 2017/12/12 下午10:44
 */
public class CastConvert {
    public static Object cast(Class cls, String value) {
        System.out.println(cls.hashCode() == int.class.hashCode());
        int targetHashCode = cls.hashCode();
        if (targetHashCode == int.class.hashCode()) {
            return Integer.parseInt(value);
        } else if (targetHashCode == Integer.class.hashCode()) {
            return Integer.parseInt(value);
        } else if (targetHashCode == String.class.hashCode()) {
            return value;
        } else if (targetHashCode == Double.class.hashCode()) {
            return Double.parseDouble(value);
        } else if (targetHashCode == Long.class.hashCode()) {
            return Long.parseLong(value);
        }
        return JsonUtils.fromJsonByJackson(value, cls);
    }

    public static void main(String[] args) {
        Object cast = cast(int.class, "23");
        System.out.println(cast);
        System.out.println(cast(Double.class,"23"));
    }
}
