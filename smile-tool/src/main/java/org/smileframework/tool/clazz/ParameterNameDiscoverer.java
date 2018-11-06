package org.smileframework.tool.clazz;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * 获取方法中变量名字
 * DefaultParameterNameDiscoverer 是对下面两个实现类的封装,会一次到实现类中加载，如果存在就返回
 *
 * StandardReflectionParameterNameDiscoverer
 * LocalVariableTableParameterNameDiscoverer
 * @author liuxin
 * @version Id: ParameterNameDiscoverer.java, v 0.1 2018/10/26 4:35 PM
 */
public interface ParameterNameDiscoverer {

    String[] getParameterNames(Method method);


    String[] getParameterNames(Constructor<?> ctor);
}
