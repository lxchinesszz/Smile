package org.smileframework.tool.common;


/**
 * @Package: smile.common
 * @Description: 默认取值工具包
 * @author: liuxin
 * @date: 2017/10/11 上午11:17
 */
public class Default {
    public static <T> T defaultValue(Object str, Object defaultValue, Class<T> cls) {
        T value = null;
        if (str == null) {
            value = ((T) defaultValue);
        } else {
            value = ((T) str);
        }
        return value;
    }

    public static <T> T defaultValue(Object str, Object defaultValue, DefaultIF<T> defaultIf) {
        T value = null;
        boolean flag = true;
        if (defaultIf != null) {
            flag = defaultIf.defaultIf();
        }
        if (!flag) {
            value = ((T) defaultValue);
        } else {
            value = ((T) str);
        }
        return value;
    }

    /**
     * 如果是true就用原始数据
     * 如果是false就要用defalueValue
     */
    public interface DefaultIF<T> {
        boolean defaultIf();
    }
}
