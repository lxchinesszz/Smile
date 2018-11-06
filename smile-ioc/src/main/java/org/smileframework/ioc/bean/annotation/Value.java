package org.smileframework.ioc.bean.annotation;

import java.lang.annotation.*;

/**
 * @author liuxin
 * @version Id: Value.java, v 0.1 2018/10/30 2:19 PM
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Value {

    /**
     * The actual value expression: e.g. "#{systemProperties.myProp}".
     * 如果是${name}会从环境中拿到name值
     * 如果没有就还是原来的值
     */
    String value();

}
