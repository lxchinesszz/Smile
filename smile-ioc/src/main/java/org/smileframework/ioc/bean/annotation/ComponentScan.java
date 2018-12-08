package org.smileframework.ioc.bean.annotation;

import java.lang.annotation.*;

/**
 * @author liuxin
 * @version Id: ComponentScan.java, v 0.1 2018/10/11 6:51 PM
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface ComponentScan {

    /**
     * 将要扫描的包
     *
     * @return
     */
    String[] scanPackages() default {};

    /**
     * 排除的包
     *
     * @return
     */
    Filter[] excludeFilters() default {};


    @Retention(RetentionPolicy.RUNTIME)
    @Target({})
    @interface Filter {

        FilterType type() default FilterType.CUSTOM;

        Class<?>[] value() default {};

        String[] pattern() default {};

    }
}
