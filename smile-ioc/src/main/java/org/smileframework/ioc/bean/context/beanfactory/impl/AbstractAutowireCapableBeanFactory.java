package org.smileframework.ioc.bean.context.beanfactory.impl;

import org.smileframework.ioc.bean.context.beandefinition.GenericBeanDefinition;
import org.smileframework.ioc.bean.context.beanfactory.AutowireCapableBeanFactory;
import org.smileframework.ioc.bean.context.postprocessor.BeanPostProcessor;
import org.smileframework.ioc.bean.context.postprocessor.InstantiationAwareBeanPostProcessor;

import java.util.Set;

/**
 * 主要操作Bean的生成
 *
 * @author liuxin
 * @version Id: AbstractAutowireCapableBeanFactory.java, v 0.1 2018/10/18 8:41 PM
 */
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory implements AutowireCapableBeanFactory {

    /**
     * 如果实例化处理器返回了Bean,就直接返回，否则创建
     * @param beanName
     * @param mbd
     * @param args
     * @return
     */
    @Override
    protected Object createBean(String beanName, GenericBeanDefinition mbd, Object[] args) {
        //先从处理器中获取，一般创建代理对象会从这里面获取，主要是继承InstantiationAwareBeanPostProcessor
        //重写实例前置处理器applyBeanPostProcessorsBeforeInstantiation方法，如果获取成功就执行初始化后方法(postProcessAfterInitialization)
        Object bean = resolveBeforeInstantiation(beanName, mbd);
        if (bean != null) {
            return bean;
        }
        /**
         * 没有在执行创建
         */
        Object beanInstance = doCreateBean(beanName, mbd, args);
        return beanInstance;
    }


    protected Object doCreateBean(final String beanName, final GenericBeanDefinition mbd, final Object[] args){
        if (mbd.isSingleton()){
            this.getSingleton(beanName);
        }
        return null;
    }


    /**
     * 解决实例前的问题
     * @param beanName
     * @param mbd
     * @return
     */
    protected Object resolveBeforeInstantiation(String beanName, GenericBeanDefinition mbd) {
        Object bean = null;
        if (!Boolean.FALSE.equals(mbd.beforeInstantiationResolved)) {
            bean = applyBeanPostProcessorsBeforeInstantiation(mbd.getBeanClass(), beanName);
            if (bean != null) {
                bean = applyBeanPostProcessorsAfterInitialization(bean, beanName);
            }
            mbd.beforeInstantiationResolved = (bean != null);
        }
        return bean;
    }

    /**
     * 应用所有的实例化处理器
     * @param beanClass
     * @param beanName
     * @return
     */
    protected Object applyBeanPostProcessorsBeforeInstantiation(Class<?> beanClass, String beanName) {

        for (BeanPostProcessor bp : getBeanPostProcessors()) {
            if (bp instanceof InstantiationAwareBeanPostProcessor) {
                InstantiationAwareBeanPostProcessor ibp = (InstantiationAwareBeanPostProcessor) bp;
                Object result = ibp.postProcessBeforeInstantiation(beanClass, beanName);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }

    @Override
    public <T> T createBean(Class<T> beanClass) throws Exception {
        return null;
    }

    @Override
    public void autowireBean(Object existingBean) throws Exception {

    }

    @Override
    public Object configureBean(Object existingBean, String beanName) throws Exception {
        return null;
    }

    @Override
    public Object resolveDependency(String beanName) throws Exception {
        return null;
    }

    @Override
    public Object createBean(Class<?> beanClass, int autowireMode, boolean dependencyCheck) throws Exception {
        return null;
    }

    @Override
    public Object autowire(Class<?> beanClass, int autowireMode, boolean dependencyCheck) throws Exception {
        return null;
    }

    @Override
    public void autowireBeanProperties(Object existingBean, int autowireMode, boolean dependencyCheck) throws Exception {

    }

    @Override
    public void applyBeanPropertyValues(Object existingBean, String beanName) throws Exception {

    }

    @Override
    public Object initializeBean(Object existingBean, String beanName) throws Exception {
        return null;
    }

    /**
     * 初始化前
     * @param existingBean
     * @param beanName
     * @return
     */
    @Override
    public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) {
        Object result = existingBean;
        for (BeanPostProcessor beanProcessor : getBeanPostProcessors()) {
            result = beanProcessor.postProcessBeforeInitialization(result, beanName);
            if (result == null) {
                return result;
            }
        }
        return result;
    }

    /**
     * 初始化后
     * @param existingBean
     * @param beanName
     * @return
     */
    @Override
    public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) {
        Object result = existingBean;
        for (BeanPostProcessor beanProcessor : getBeanPostProcessors()) {
            result = beanProcessor.postProcessAfterInitialization(result, beanName);
            if (result == null) {
                return result;
            }
        }
        return result;
    }

    @Override
    public void destroyBean(Object existingBean) {

    }

    @Override
    public Object resolveDependency(String beanName, Set<String> autowiredBeanNames) throws Exception {
        return null;
    }
}
