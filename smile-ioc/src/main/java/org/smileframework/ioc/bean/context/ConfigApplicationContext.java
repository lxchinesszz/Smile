package org.smileframework.ioc.bean.context;

import org.smileframework.tool.date.StopWatch;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * @Package: org.smileframework.ioc.bean.context
 * @Description: 配置上线文
 * @author: liuxin
 * @date: 2017/12/6 下午6:01
 */
public interface ConfigApplicationContext {

    public  StopWatch getStopWatch();

    Map<String, BeanDefinition> getBeans();

    Object getBean(String var1);

    <T> T getBean(String name, Class<T> requiredType);

    <T> T getBean(Class<T> name);

    boolean containsBean(String var1);

    ConfigurableEnvironment getConfigurableEnvironment();

//    void addExtApplication(ExtApplicationContext extApplicationContext);

    Map<String, BeanDefinition> getBeanByAnnotation(Class<? extends Annotation> cls);
}
