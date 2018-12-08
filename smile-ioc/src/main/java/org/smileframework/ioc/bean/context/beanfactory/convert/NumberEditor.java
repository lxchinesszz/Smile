package org.smileframework.ioc.bean.context.beanfactory.convert;

import cn.hutool.core.util.NumberUtil;


/**
 * @author liuxin
 * @version Id: NumberEditor.java, v 0.1 2018-12-06 14:24
 */
public class NumberEditor implements PropertyConvertEditor {

  @Override
  public <T> T doConvertIfNecessary(Object value, Class<T> requiredType) {
    if (value instanceof String) {
      String valueString = String.valueOf(value);
      if (NumberUtil.isNumber(valueString)) {
        if (requiredType == Double.class) {
          return (T) Double.valueOf(Double.parseDouble(valueString));
        }
        if (requiredType == double.class) {
          return (T) Double.valueOf(Double.parseDouble(valueString));
        }
        if (requiredType == Integer.class) {
          return (T) Integer.valueOf(valueString);
        }
        if (requiredType == int.class) {
          return (T) Integer.valueOf(valueString);
        }

        if (requiredType == Long.class) {
          return (T) Long.valueOf(valueString);
        }
        if (requiredType == long.class) {
          return (T) Long.valueOf(valueString);
        }
        if (requiredType == Byte.class) {
          return (T) Byte.valueOf(valueString);
        }
        if (requiredType == byte.class) {
          return (T) Byte.valueOf(valueString);
        }
        if (requiredType == Float.class) {
          return (T) Float.valueOf(valueString);
        }
        if (requiredType == float.class) {
          return (T) Float.valueOf(valueString);
        }

      }
    }
    return (T) value;
  }

}
