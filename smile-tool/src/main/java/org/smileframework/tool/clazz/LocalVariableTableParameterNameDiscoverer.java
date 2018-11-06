package org.smileframework.tool.clazz;

import javassist.*;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

/**
 * @Package: org.smileframework.tool.clazz
 * @Description: 获取形参名称
 * @author: liuxin
 * @date: 2017/12/4 下午8:03
 */
public class LocalVariableTableParameterNameDiscoverer implements ParameterNameDiscoverer {



    @Override
    public String[] getParameterNames(Method method) {
        //declar不局限与修饰符
        Class clazz = method.getDeclaringClass();
        String methodName = method.getName();
        ClassPool pool = ClassPool.getDefault();
        pool.insertClassPath(new ClassClassPath(clazz));
        try {
            CtClass cc = pool.get(clazz.getName());
            CtMethod cm = cc.getDeclaredMethod(methodName);
            MethodInfo methodInfo = cm.getMethodInfo();
            CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
            LocalVariableAttribute attr =
                    (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
            if (attr == null) {
                return null;
            }
            String[] paramNames = new String[cm.getParameterTypes().length];
            int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
            for (int i = 0; i < paramNames.length; i++) {
                paramNames[i] = attr.variableName(i + pos);
            }
            return paramNames;
        } catch (Exception e) {

        }
        return null;
    }

    @Override
    public String[] getParameterNames(Constructor<?> ctor) {
        Class<?> declaringClass = ctor.getDeclaringClass();
        ClassPool pool = ClassPool.getDefault();
        pool.insertClassPath(new ClassClassPath(declaringClass));
        try {
            CtClass ctClass = pool.get(declaringClass.getName());
            CtConstructor declaredConstructor = ctClass.getDeclaredConstructor(ctClasses(ctor));
            MethodInfo methodInfo = declaredConstructor.getMethodInfo();
            CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
            LocalVariableAttribute attr =
                    (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
            if (attr == null) {
                return null;
            }
            String[] paramNames = new String[declaredConstructor.getParameterTypes().length];
            int pos = Modifier.isStatic(declaredConstructor.getModifiers()) ? 0 : 1;
            for (int i = 0; i < paramNames.length; i++) {
                paramNames[i] = attr.variableName(i + pos);
            }
            return paramNames;
        } catch (Exception e) {

        }

        return new String[0];
    }

    public CtClass[] ctClasses(Constructor<?> ctor) {
        Class<?>[] parameterTypes = ctor.getParameterTypes();
        ClassPool pool = ClassPool.getDefault();
        CtClass[] ctClasses = new CtClass[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> parameterType = parameterTypes[i];
//            pool.insertClassPath(new ClassClassPath(parameterType));
            CtClass ctClass = null;
            try {
                ctClass = pool.get(parameterType.getName());
            } catch (NotFoundException e) {
                e.printStackTrace();
            }
            ctClasses[i] = ctClass;
        }
        return ctClasses;
    }

    public static void main(String[] args) throws Exception {
        Constructor<?>[] declaredConstructors = LocalVariableTableParameterNameDiscoverer.class.getDeclaredConstructors();
        String[] parameterNames = new LocalVariableTableParameterNameDiscoverer().getParameterNames(declaredConstructors[0]);
        Arrays.stream(parameterNames).forEach(System.out::print);
//        Method method = LocalVariableTableParameterName.class.getMethods()[1];
//        LocalVariableTableParameterName localVariableTableParameterName = new LocalVariableTableParameterName();
//        String[] parameterNames = localVariableTableParameterName.getParameterNames(method);
//        Arrays.asList(parameterNames).forEach(x->{
//            System.out.println(x);
//        });
//        Demo demo = new Demo();
//        String name = demo.getClass().getMethods()[0].toGenericString();
//        System.out.println(name);

    }
}
