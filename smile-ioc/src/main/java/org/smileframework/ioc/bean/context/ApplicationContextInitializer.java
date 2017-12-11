package org.smileframework.ioc.bean.context;


/**
 * @Package: pig.boot.ioc.context
 * @Description: 上下文
 * @author: liuxin
 * @date: 2017/11/17 下午11:52
 */
public interface ApplicationContextInitializer<C extends ApplicationContext> {
    ConfigurableApplicationContext initialize(C applicationContext);
}