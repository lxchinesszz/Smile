package org.smileframework.ioc.bean.annotation;

import java.lang.annotation.*;

/**
 * @author liuxin
 * @version Id: Autowired.java, v 0.1 2018/10/30 2:14 PM
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Autowired {

    /**
     * 是否必须依赖
     * true强制依赖,如果注入失败,直接报错
     * false不强制依赖,失败就不注入,默认就是null,此时在调用时候会出现空指针的情况,需要用户自己去捕捉
     * @return
     */
    boolean required() default true;
}
