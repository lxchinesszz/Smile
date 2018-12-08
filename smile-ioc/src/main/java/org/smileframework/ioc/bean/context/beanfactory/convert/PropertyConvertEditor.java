package org.smileframework.ioc.bean.context.beanfactory.convert;

/**
 * @author liuxin
 * @version Id: PropertyEditor.java, v 0.1 2018-12-06 14:09
 */
public interface PropertyConvertEditor {
  default <T> T doConvertIfNecessary(Object value, Class<T> requiredType) {
    if (requiredType == String.class) {
      return (T) value;
    }
    return null;
  }
}
