package org.smileframework.ioc.bean.context.postprocessor;

import java.beans.PropertyDescriptor;

/**
 * 提供实例化前后处理器
 * @author liuxin
 * @version Id: InstantiationAwareBeanPostProcessor.java, v 0.1 2018/10/18 9:15 PM
 */
public interface InstantiationAwareBeanPostProcessor extends BeanPostProcessor {


    /**
     * 前实例化接口
     * @param beanClass
     * @param beanName
     * @return
     * @throws Exception
     */
    Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) ;


    /**
     * 后实例化
     * @param bean
     * @param beanName
     * @return
     * @throws Exception
     */
    boolean postProcessAfterInstantiation(Object bean, String beanName) ;


    void postProcessPropertyValues(Object bean, String beanName);
}
