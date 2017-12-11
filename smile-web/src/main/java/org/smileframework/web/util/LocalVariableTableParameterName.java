package org.smileframework.web.util;

import javassist.*;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

/**
 * @Package: org.smileframework.tool.clazz
 * @Description: 获取形参名称
 * @author: liuxin
 * @date: 2017/12/4 下午8:03
 */
public class LocalVariableTableParameterName {

    public static String[] getParameterNames(Method method){
        //declar不局限与修饰符
        Class clazz = method.getDeclaringClass();
        String methodName = method.getName();
        ClassPool pool = ClassPool.getDefault();
        pool.insertClassPath(new ClassClassPath(clazz));
        CtMethod cm = null;
        LocalVariableAttribute attr =
                null;
        String[] paramNames = new String[0];
        try {
            CtClass cc = pool.get(clazz.getName());
            cm = cc.getDeclaredMethod(methodName);
            MethodInfo methodInfo = cm.getMethodInfo();
            CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
            attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
            if (attr == null) {
                return null;
            }
            paramNames = new String[cm.getParameterTypes().length];
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
        for (int i = 0; i < paramNames.length; i++) {
            paramNames[i] = attr.variableName(i + pos);
        }
        return paramNames;
    }

    public static void main(String[] args)throws Exception{
        Method method = org.smileframework.tool.clazz.LocalVariableTableParameterName.class.getMethods()[1];
        org.smileframework.tool.clazz.LocalVariableTableParameterName localVariableTableParameterName = new org.smileframework.tool.clazz.LocalVariableTableParameterName();
        String[] parameterNames = localVariableTableParameterName.getParameterNames(method);
        Arrays.asList(parameterNames).forEach(x->{
            System.out.println(x);
        });
//        Demo demo = new Demo();
//        Method[] methods = demo.getClass().getDeclaredMethods();
//        Class clazz = methods[0].getDeclaringClass();
//        String methodName = methods[0].getName();
//        ClassPool pool = ClassPool.getDefault();
//        pool.insertClassPath(new ClassClassPath(clazz));
//        CtClass cc = pool.get(clazz.getName());
//        CtMethod cm = cc.getDeclaredMethod(methodName);
//        MethodInfo methodInfo = cm.getMethodInfo();
//        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
//        LocalVariableAttribute attr =
//                (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
//        if (attr == null) {
//            System.out.println("params is null");
//        }
//        String[] paramNames = new String[cm.getParameterTypes().length];
//        int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
//        for (int i = 0; i < paramNames.length; i++)
//            paramNames[i] = attr.variableName(i + pos);
//        for (int i = 0; i < paramNames.length; i++) {
//            System.out.println(paramNames[i]);
//        }
    }
}
