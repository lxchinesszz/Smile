package org.smileframework.tool.annotation;

/**
 * @Package: org.smileframework.tool.annotation
 * @Description:
 * @author: liuxin
 * @date: 2018/1/3 上午11:56
 */
public class AnnotationConfigurationException extends RuntimeException{
    public AnnotationConfigurationException(String message) {
        super(message);
    }

    public AnnotationConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
