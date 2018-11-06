package org.smileframework.tool.clazz;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * 标准的通过反射获取
 * @author liuxin
 * @version Id: StandardReflectionParameterNameDiscoverer.java, v 0.1 2018/10/26 4:36 PM
 */
public class StandardReflectionParameterNameDiscoverer implements ParameterNameDiscoverer {

    @Override
    public String[] getParameterNames(Method method) {
        Parameter[] parameters = method.getParameters();
        String[] parameterNames = new String[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            Parameter param = parameters[i];
            if (!param.isNamePresent()) {
                return null;
            }
            parameterNames[i] = param.getName();
        }
        return parameterNames;
    }

    @Override
    public String[] getParameterNames(Constructor<?> ctor) {
        Parameter[] parameters = ctor.getParameters();
        String[] parameterNames = new String[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            Parameter param = parameters[i];
            if (!param.isNamePresent()) {
                return null;
            }
            parameterNames[i] = param.getName();
        }
        return parameterNames;
    }

}
