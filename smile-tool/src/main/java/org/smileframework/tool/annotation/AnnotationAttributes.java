package org.smileframework.tool.annotation;

import java.util.HashMap;

/**
 * @Package: org.smileframework.tool.annotation
 * @Description: ${todo}
 * @author: liuxin
 * @date: 2018/1/4 下午3:29
 */
public class AnnotationAttributes extends HashMap {
    private String annotationName;
    private Class annotationType;
    private Object annotationValue;

    public AnnotationAttributes(String annotationName,Class annotationType){
        this.annotationName=annotationName;
        this.annotationType=annotationType;
    }

    public String getAnnotationName(){
        return this.annotationName;
    }

    public Class getAnnotationType(){
        return this.annotationType;
    }

}
