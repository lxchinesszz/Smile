package org.smileframework.ioc.bean.context;

import org.smileframework.ioc.bean.context.beandefinition.BeanDefinition;

/**
 * 负责将BeanDefinition注册到BeanFactory
 * 主要给BeanFactory实现
 * @author liuxin
 * @version Id: BeanDefinitionRegistry.java, v 0.1 2018/10/11 11:16 AM
 */
public interface BeanDefinitionRegistry {

    /**
     * 注册BeanDefinition
     * @param beanName
     * @param beanDefinition
     */
    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);

    /**
     * 根据BeanName移除BeanDefinition
     * @param beanName
     */
    void removeBeanDefinition(String beanName);

    /**
     * 根据名字获取BeanDefinition
     * @param beanName
     * @return
     */
    BeanDefinition getBeanDefinition(String beanName);

    /**
     * 根据名字判断是否包含有改bean
     * @param beanName
     * @return
     */
    boolean containsBeanDefinition(String beanName);

    /**
     * 获取所有的BeanName集合
     * @return
     */
    String[] getBeanDefinitionNames();

    /**
     * 获取当前BeanDefinition的数量
     * @return
     */
    int getBeanDefinitionCount();

}
