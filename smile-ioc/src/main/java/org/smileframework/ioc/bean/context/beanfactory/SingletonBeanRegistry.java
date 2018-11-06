package org.smileframework.ioc.bean.context.beanfactory;

/**
 * @author liuxin
 * @version Id: SingletonBeanRegistry.java, v 0.1 2018/10/29 2:21 PM
 */
public interface SingletonBeanRegistry {
    /**
     * 将单例的Bean通过该方法缓存起来
     * @param beanName
     * @param singletonObject
     */
    void registerSingleton(String beanName, Object singletonObject);


    /**
     * 根据名字获取单例
     * @param beanName
     * @return
     */
    Object getSingleton(String beanName);
}
