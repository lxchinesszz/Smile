package org.smileframework.ioc.bean.annotation;

/**
 * @Package: org.smileframework.ioc.bean.annotation
 * @Description: 扫描类
 * @author: liuxin
 * @date: 2017/12/14 下午3:06
 */

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
public @interface SmileService {
}
