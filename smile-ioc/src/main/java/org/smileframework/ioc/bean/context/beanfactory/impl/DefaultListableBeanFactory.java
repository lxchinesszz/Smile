package org.smileframework.ioc.bean.context.beanfactory.impl;

import org.smileframework.ioc.bean.context.beandefinition.BeanDefinition;
import org.smileframework.ioc.bean.context.BeanDefinitionRegistry;
import org.smileframework.ioc.bean.context.beanfactory.BeanFactory;
import org.smileframework.ioc.bean.context.beanfactory.impl.AbstractAutowireCapableBeanFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * AbstractAutowireCapableBeanFactory提供了创建Bean和注入的方法
 * BeanDefinitionRegistry 提供了操作BeanDefinitionMap的方法
 * 维护和BeanDefinitionMap的关系
 *
 * @author liuxin
 * @version Id: DefaultListableBeanFactory.java, v 0.1 2018/10/11 11:21 AM
 */
public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory implements BeanFactory, BeanDefinitionRegistry {

    /**
     * 保存Bean的描述信息
     */
    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, BeanDefinition>(64);


    /**
     * 实现BeanDefinitionRegistry相关方法
     */
    //---------------------------------------------------------------------
    // Implementation of BeanDefinitionRegistry interface
    //---------------------------------------------------------------------

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        this.beanDefinitionMap.put(beanName, beanDefinition);
    }

    @Override
    public void removeBeanDefinition(String beanName) {
        this.beanDefinitionMap.remove(beanName);
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanName) {
        return this.beanDefinitionMap.get(beanName);
    }

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return this.beanDefinitionMap.containsKey(beanName);
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return ((String[]) this.beanDefinitionMap.keySet().toArray());
    }

    @Override
    public int getBeanDefinitionCount() {
        return this.beanDefinitionMap.size();
    }


    /**实现Bean工厂相关的接口方法 */
    @Override
    public <T> T getBean(Class<T> requiredType) {
        //根据类型注入要先去找FactoryBean
        return null;
    }

    @Override
    public Class getType(String name) {
        return null;
    }

    @Override
    public boolean isFactoryBean(String name) {
        return false;
    }
}
