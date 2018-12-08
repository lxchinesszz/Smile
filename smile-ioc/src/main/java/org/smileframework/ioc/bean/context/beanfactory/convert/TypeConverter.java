package org.smileframework.ioc.bean.context.beanfactory.convert;

/**
 * @author liuxin
 * @version Id: TypeConverter.java, v 0.1 2018/11/19 3:11 PM
 */
public interface TypeConverter {
    /**
     * 转换成需求的类型
     * @param value
     * @param requiredType
     * @param <T>
     * @return
     */
    <T> T convertIfNecessary(Object value,Class<T> requiredType);
}
