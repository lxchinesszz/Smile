package org.smileframework.tool.clazz;

import java.lang.reflect.Method;

/**
 * @author liuxin
 * @version Id: MethodTools.java, v 0.1 2018-12-04 15:44
 */
public class MethodTools {

  public static Method findMethodByName(Class cls, String methodName,Class<?>... parameterTypes) {
    try {
      return cls.getDeclaredMethod(methodName, parameterTypes);
    } catch (Exception e) {
    }
    return null;
  }
}
