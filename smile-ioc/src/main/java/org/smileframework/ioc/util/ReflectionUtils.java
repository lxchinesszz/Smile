package org.smileframework.ioc.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @author liuxin
 * @version Id: ReflectionUtils.java, v 0.1 2018/10/29 6:40 PM
 */
public class ReflectionUtils {

    public static void makeAccessible(Field field) {
        if ((!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers()) ||
                Modifier.isFinal(field.getModifiers())) && !field.isAccessible()) {
            field.setAccessible(true);
        }
    }


    public static void makeAccessible(Method method) {
        if ((!Modifier.isPublic(method.getModifiers()) || !Modifier.isPublic(method.getDeclaringClass().getModifiers()))
                && !method.isAccessible()) {
            method.setAccessible(true);
        }
    }
}
