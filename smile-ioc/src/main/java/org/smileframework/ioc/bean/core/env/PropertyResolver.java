package org.smileframework.ioc.bean.core.env;

/**
 * 属性解析器通用接口
 *
 * @author liuxin
 * @version Id: PropertyResolver.java, v 0.1 2018/10/15 11:11 AM
 */
public interface PropertyResolver {


    boolean containsProperty(String key);


    String getProperty(String key);


    String getProperty(String key, String defaultValue);


    <T> T getProperty(String key, Class<T> targetType);


    <T> T getProperty(String key, Class<T> targetType, T defaultValue);


    <T> Class<T> getPropertyAsClass(String key, Class<T> targetType);

    /**
     * 带Required的方法，都是一样要有的，没有就报错
     */

    String getRequiredProperty(String key) throws IllegalStateException;


    <T> T getRequiredProperty(String key, Class<T> targetType) throws IllegalStateException;

    /**
     * Resolve ${...} placeholders in the given text, replacing them with corresponding
     *
     * @param text
     * @return
     */
    String resolvePlaceholders(String text);


    String resolveRequiredPlaceholders(String text) throws IllegalArgumentException;
}
