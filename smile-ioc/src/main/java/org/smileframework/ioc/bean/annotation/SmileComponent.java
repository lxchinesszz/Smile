package org.smileframework.ioc.bean.annotation;

import java.lang.annotation.*;

/**
 * @Package: pig.boot.ioc.annotation
 * @Description: 需要ioc扫描的组件组件
 * @author: liuxin
 * @date: 2017/11/17 下午11:37
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface SmileComponent {

    String[] basePackages() default {};

    String name() default "";
}
