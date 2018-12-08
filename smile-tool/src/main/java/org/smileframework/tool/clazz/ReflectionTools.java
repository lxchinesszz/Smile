package org.smileframework.tool.clazz;

import javassist.*;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;
import org.smileframework.tool.string.StringTools;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Package: org.smileframework.tool.clazz
 * @Description: 反射工具
 * @author: liuxin
 * @date: 2017/12/12 下午7:19
 */
public class ReflectionTools {


    public static List<Method> getMethod(Object obj) {
        return Arrays.asList(obj.getClass().getDeclaredMethods());
    }



    public static void makeAccessible(Field field) {
        if ((!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers()) || Modifier.isFinal(field.getModifiers())) && !field.isAccessible()) {
            field.setAccessible(true);
        }

    }

    public static void makeAccessible(Method method) {
        if ((!Modifier.isPublic(method.getModifiers()) || !Modifier.isPublic(method.getDeclaringClass().getModifiers())) && !method.isAccessible()) {
            method.setAccessible(true);
        }

    }

    public static void makeAccessible(Constructor<?> ctor) {
        if ((!Modifier.isPublic(ctor.getModifiers()) || !Modifier.isPublic(ctor.getDeclaringClass().getModifiers())) && !ctor.isAccessible()) {
            ctor.setAccessible(true);
        }

    }
    /**
     * 获取指定对象的方法
     * @param obj
     * @param methodName
     * @return
     */
    public static List<Method> getMethod(Object obj, String methodName) {
        Object[] objects = getMethod(obj).stream().filter(m -> {
            if (StringTools.isNotEmpty(methodName)) {
                return m.getName().contains(methodName);
            } else {
                return true;
            }
        }).toArray();
        return ClassTools.castByArray(objects, Method.class);
    }


    /**
     * 根据方法,获取参数名称
     * @param method
     * @return
     */
    public static String[] getParameterNames(Method method) {
        //declar不局限与修饰符
        Class clazz = method.getDeclaringClass();
        if (clazz.getSimpleName().equalsIgnoreCase("Object")) {
            throw new IllegalArgumentException(clazz.getSimpleName() + ":类不能被解析");
        }
        String methodName = method.getName();
        ClassPool pool = ClassPool.getDefault();
        pool.insertClassPath(new ClassClassPath(clazz));
        CtMethod cm = null;
        try {
            CtClass cc = pool.get(clazz.getName());
            cm = cc.getDeclaredMethod(methodName);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        MethodInfo methodInfo = cm.getMethodInfo();
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
        LocalVariableAttribute attr =
                (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
        if (attr == null) {
            return null;
        }
        String[] paramNames = new String[0];
        try {
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

    /**
     * 获取方法,参数信息
     *
     * @param method
     * @param annotation
     * @return
     */
    public static List<ParamDefinition> getParameterDefinitions(Method method, Class<? extends Annotation> annotation) {
        Parameter[] parameters = method.getParameters();
        String[] parameterNames = getParameterNames(method);
        List<ParamDefinition> paramDefinitions = new ArrayList<>(parameters.length);
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            Annotation annotationDefinition = parameter.getAnnotation(annotation);
            if (annotationDefinition == null) {
                continue;
            }
            Class<?> type = parameter.getType();
            String argIndex = getArgIndex(parameter.getName());
            String parameterName = parameterNames[Integer.parseInt(argIndex)];
            Annotation[] annotations = new Annotation[1];
            annotations[0] = annotationDefinition;
            paramDefinitions.add(new ParamDefinition(argIndex, parameterName, annotations, type));
        }
        return paramDefinitions;
    }

    /**
     * 获取方法,参数信息
     *
     * @param method
     * @return
     */
    public static List<ParamDefinition> getParameterDefinitions(Method method) {
        Parameter[] parameters = method.getParameters();
        String[] parameterNames = getParameterNames(method);
        List<ParamDefinition> paramDefinitions = new ArrayList<>(parameters.length);
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            Annotation[] declaredAnnotations = parameter.getDeclaredAnnotations();
            Class<?> type = parameter.getType();
            String argIndex = getArgIndex(parameter.getName());
            String parameterName = parameterNames[Integer.parseInt(argIndex)];
            paramDefinitions.add(new ParamDefinition(argIndex, parameterName, declaredAnnotations, type));
        }
        return paramDefinitions;
    }


    private static String getArgIndex(String arg) {
        String argIndex = arg.replaceAll("arg", "");
        return argIndex;
    }


    public static Annotation getAnnotation(Annotation[] annotations, Class<? extends Annotation> annotation) {
        for (int i = 0; i < annotations.length; i++) {
            Annotation srcAnnotation = annotations[i];
            if (srcAnnotation.annotationType().isAssignableFrom(annotation)) {
                return srcAnnotation;
            }
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        Demo demo = new Demo();
        Method[] methods = demo.getClass().getMethods();
        Method method = methods[1];
        //只查询当前方法中,指定的注解
        List<ParamDefinition> parameterDefinitions = getParameterDefinitions(method, ParamKey.class);
        parameterDefinitions.stream().forEach(paramDefinition -> {
            System.out.println("只查询当前方法中,指定的注解:" + paramDefinition);
            Annotation[] declaredAnnotations = paramDefinition.getDeclaredAnnotations();
            Annotation annotation = getAnnotation(declaredAnnotations, ParamKey.class);
            System.out.println(annotation);
        });


        List<ParamDefinition> parameterDefinitionss = getParameterDefinitions(method);
        parameterDefinitionss.stream().forEach(paramDefinition -> {
            System.out.println("查询当前方法中,所有参数:" + paramDefinition);
        });

        Class<?> aClass = Class.forName("org.smileframework.tool.clazz.Dto");
        System.out.println(aClass);

    }

    public static class ParamDefinition {

        private String argIndex;

        private String paramName;

        private Annotation[] declaredAnnotations;

        //name 是int
        private Class paramType;

        public ParamDefinition(String argIndex, String paramName, Annotation[] declaredAnnotations, Class paramType) {
            this.argIndex = argIndex;
            this.paramName = paramName;
            this.declaredAnnotations = declaredAnnotations;
            this.paramType = paramType;
        }

        public String getArgIndex() {
            return argIndex;
        }

        public void setArgIndex(String argIndex) {
            this.argIndex = argIndex;
        }

        public String getParamName() {
            return paramName;
        }

        public void setParamName(String paramName) {
            this.paramName = paramName;
        }

        public Annotation[] getDeclaredAnnotations() {
            return declaredAnnotations;
        }

        public void setDeclaredAnnotations(Annotation[] declaredAnnotations) {
            this.declaredAnnotations = declaredAnnotations;
        }

        public Class getParamType() {
            return paramType;
        }

        public void setParamType(Class paramType) {
            this.paramType = paramType;
        }

        @Override
        public String toString() {
            return "ParamDefinition{" +
                    "argIndex='" + argIndex + '\'' +
                    ", paramName='" + paramName + '\'' +
                    ", declaredAnnotations=" + Arrays.toString(declaredAnnotations) +
                    ", paramType=" + paramType +
                    '}';
        }
    }
}
