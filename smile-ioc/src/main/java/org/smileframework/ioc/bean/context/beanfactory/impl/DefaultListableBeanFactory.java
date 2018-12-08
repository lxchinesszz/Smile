package org.smileframework.ioc.bean.context.beanfactory.impl;

import org.smileframework.ioc.bean.context.beandefinition.BeanDefinition;
import org.smileframework.ioc.bean.context.BeanDefinitionRegistry;
import org.smileframework.ioc.bean.context.beandefinition.GenericBeanDefinition;
import org.smileframework.ioc.bean.context.beanfactory.BeanFactory;
import org.smileframework.ioc.bean.context.beanfactory.ListableBeanFactory;
import org.smileframework.ioc.bean.context.beanfactory.convert.TypeConverter;
import org.smileframework.ioc.bean.context.beanfactory.convert.TypeConverterSupport;
import org.smileframework.ioc.bean.context.factorybean.FactoryBean;
import org.smileframework.ioc.bean.context.postprocessor.impl.DependencyDescriptor;
import org.smileframework.tool.asserts.Assert;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * AbstractAutowireCapableBeanFactory提供了创建Bean和注入的方法
 * BeanDefinitionRegistry 提供了操作BeanDefinitionMap的方法
 * 维护和BeanDefinitionMap的关系
 *
 * @author liuxin
 * @version Id: DefaultListableBeanFactory.java, v 0.1 2018/10/11 11:21 AM
 */
public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory implements ListableBeanFactory, BeanDefinitionRegistry {

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
        String[] strings = this.beanDefinitionMap.keySet().toArray(new String[]{});
        return strings;
    }

    @Override
    public int getBeanDefinitionCount() {
        return this.beanDefinitionMap.size();
    }




    /**
     * 实现Bean工厂相关的接口方法
     */
    @Override
    public <T> T getBean(Class<T> requiredType) {
        Assert.notNull(requiredType, "Required type must not be null");
        //根据类型注入要先去找FactoryBean,判断是否是抽象和接口，如果是
        if (requiredType.getGenericSuperclass() instanceof FactoryBean) {

        }
        return null;
    }

    @Override
    public Class getType(String name) {
        return containsBean(name)?getBeanDefinition(name).getBeanClass():null;
    }

    @Override
    public boolean isFactoryBean(String name) {
        if (containsBeanDefinition(name)) {
            BeanDefinition beanDefinition = getBeanDefinition(name);
            return beanDefinition.isFactoryBean();
        }
        return false;
    }

    /**
     * @param descriptor
     * @param requestBeanName    可以根据请求的BeanName找到BeanDefinition
     * @param autowiredBeanNames 自动注入的beanName
     * @param typeConverter
     * @return
     */
    @Override
    public Object resolveDependency(DependencyDescriptor descriptor, String requestBeanName,
                                    Set<String> autowiredBeanNames, TypeConverter typeConverter) {
        Class<?> dependencyType = descriptor.getDependencyType();
        String dependencyBeanName = beanNameGenerator.generateBeanName(dependencyType);
        return getBean(dependencyBeanName);
    }



    /**
     * 根据类型找到相同类型的BeanName。
     * 什么时候会使用这个呢?
     * 当一个bean需要注入一个接口或者是抽象类时候,众所周知接口和抽象类是不能够实力化的,这个时候我们就需要
     * 根据接口或者抽象类的类型找到，相同类型的beanName从而来执行注入,当只找到一个时候就可以实现注入
     * 但是当一个类型的接口或者抽象类，找到了多个BeanName,此时就不能正常注入,因为IOC容器并不知道你要注入那个
     * 这个时候你就要根据BeanName来指定注入了
     * @param var1
     * @return
     */
    @Override
    public String[] getBeanNamesForType(Class<?> var1) {
        List<String> beanNamelist = new ArrayList<>();
        Iterator<BeanDefinition> iterator = beanDefinitionMap.values().iterator();
        while (iterator.hasNext()) {
            GenericBeanDefinition beanDefinition = (GenericBeanDefinition) iterator.next();
            Class beanClass = beanDefinition.getBeanClass();
            //是否匹配
            if (!beanClass.equals(var1) && var1.isAssignableFrom(beanClass)) {
                beanNamelist.add(beanDefinition.getBeanName());
            }
        }
        return beanNamelist.toArray(new String[beanNamelist.size()]);
    }

    @Override
    public boolean containsBean(String name) {
        return beanDefinitionMap.keySet().contains(name);
    }

    /**
     * 返回一个副本,防止被外包恶意修改
     * @return
     */
    @Override
    public Map<String, BeanDefinition> getBeanDefinitioin() {
        return new ConcurrentHashMap<>(beanDefinitionMap);
    }

}


