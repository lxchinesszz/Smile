package org.smileframework.tool.annotation;

import org.smileframework.tool.clazz.ClassTools;
import org.smileframework.tool.clazz.Demo;
import org.smileframework.tool.clazz.ReflectionTools;
import org.smileframework.tool.string.ObjectTools;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Package: org.smileframework.tool.annotation
 * @Description: 注解工具
 * @author: liuxin
 * @date: 2018/1/3 上午10:55
 */
public class AnnotationTools {
    private static final Map<Class<? extends Annotation>, List<AnnotationAttributes>> attributeAliasesCache = new ConcurrentHashMap<>(256);

    public static List<Annotation> getAnnotationFromMethod(Method method) {
        return Arrays.asList(method.getAnnotations());
    }

    /**
     * 获取注解对象参数值
     *
     * @param annotation
     * @return
     */
    public static AnnotationMap<String, Object> getAnnotationAttributeAsMap(Annotation annotation) {
        List<AnnotationAttributes> annotationAttributes = getAnnotationAttributes(annotation.annotationType());
        Map<String, Object> annotationValue = new ConcurrentHashMap<>();
        for (AnnotationAttributes annotationAttr : annotationAttributes) {
            String annotationKey = annotationAttr.getAnnotationName();
            Object o = null;
            try {
                Method declaredMethod = annotation.annotationType().getDeclaredMethod(annotationKey);
                o = declaredMethod.invoke(annotation, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
            annotationValue.put(annotationKey, o);
        }

        return new AnnotationMap<>(annotationValue);
    }


    /**
     * 返回当前注解的所有字段
     *
     * @param annotationType
     * @return
     */
    static List<AnnotationAttributes> getAnnotationAttributes(Class<? extends Annotation> annotationType) {
        List<AnnotationAttributes> annotationAttributes = attributeAliasesCache.get(annotationType);
        if (ObjectTools.isNotEmpty(annotationAttributes)) {
            return annotationAttributes;
        }
        List<Method> methods = getAttributeMethods(annotationType);
        List<AnnotationAttributes> annotationAttributesList = new ArrayList<>();
        methods.stream().forEach(method -> {
            annotationAttributesList.add(new AnnotationAttributes(method.getName(), method.getReturnType()));
        });
        attributeAliasesCache.put(annotationType, annotationAttributesList);
        return annotationAttributesList;
    }

    /**
     * 获取注解中所有的字段名
     *
     * @param annotationType
     * @return
     */
    static List<Method> getAttributeMethods(Class<? extends Annotation> annotationType) {
        Method[] declaredMethods = annotationType.getDeclaredMethods();
        return ClassTools.castByArray(declaredMethods, Method.class);
    }


    public static void main(String[] args) throws Exception {
        List<Method> add = ReflectionTools.getMethod(new Demo(), "add");
        Method method = add.get(0);
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        Annotation annotation = parameterAnnotations[0][0];
        Map<String, Object> annotationAttributes = AnnotationTools.getAnnotationAttributeAsMap(annotation);
        annotationAttributes.entrySet().forEach(name -> System.out.println(name.getKey() + "--" + name.getValue()));

        String name = AnnotationTools.getAnnotationAttributeAsMap(annotation).getString("name");
        System.out.println(name);


    }

}
