package org.smileframework.tool.clazz;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;


/**
 * @Package: reflect.modle
 * @Description: ${todo}
 * @author: liuxin
 * @date: 2017/9/18 下午3:49
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(PARAMETER)
public @interface ParamKey {
    String value();

    String name() default "";
}
