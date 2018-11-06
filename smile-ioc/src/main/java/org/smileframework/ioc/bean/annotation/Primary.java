package org.smileframework.ioc.bean.annotation;

import java.lang.annotation.*;

/**
 * 该注解的使用，很少见，主要是用于当获取Bean时候，此时容器中存在多个
 * Bean的名字一样而且类型一样的实例。此时会报错，提示不知道用哪个Bean
 * 但是如果加上了，@Primary 那么加这个Primary的类就会返回。
 * @author liuxin
 * @version Id: Primary.java, v 0.1 2018/10/26 4:18 PM
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Primary {

}
