package org.smileframework.ioc.bean.annotation;

import org.smileframework.ioc.bean.context.beanfactory.ConfigurableBeanFactory;

import java.lang.annotation.*;

/**
 * @author liuxin
 * @version Id: Scope.java, v 0.1 2018/10/26 3:40 PM
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Scope {

    /**
     * 默认为单例
     * 所谓单例就是IOC容器中只有一个
     * SCOPE_SINGLETON = "singleton";
     *
     * 原型模式
     * 所谓原型就是IOC不保存这个实例,每次调用实例,都是重新创建的
     * SCOPE_PROTOTYPE = "prototype";
     * @return
     */
    String value() default ConfigurableBeanFactory.SCOPE_SINGLETON;


}
