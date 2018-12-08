package org.smileframework.ioc.bean.context.beanfactory;

import org.smileframework.ioc.bean.context.beandefinition.GenericBeanDefinition;
import org.smileframework.ioc.bean.context.beanfactory.convert.TypeConverter;
import org.smileframework.ioc.bean.context.postprocessor.impl.DependencyDescriptor;

import java.util.Set;

/**
 * 提供根据注入依赖的方法
 * @author liuxin
 * @version Id: AutowireCapableBeanFactory.java, v 0.1 2018/10/18 4:17 PM
 */
public interface AutowireCapableBeanFactory extends BeanFactory {

    /**
     * 根据名字注入
     */
    int AUTOWIRE_BY_NAME = 1;

    /**
     * 根据类型注入
     */
    int AUTOWIRE_BY_TYPE = 2;

    /**
     * 根据构造注入
     */
    int AUTOWIRE_CONSTRUCTOR = 3;


    <T> T createBean(Class<T> beanClass) throws Exception;


    void autowireBean(Object existingBean) throws Exception;


    Object configureBean(Object existingBean, String beanName) throws Exception;


    Object resolveDependency( String beanName) throws Exception;


    //-------------------------------------------------------------------------
    // Specialized methods for fine-grained control over the bean lifecycle
    //-------------------------------------------------------------------------

    Object createBean(Class<?> beanClass, int autowireMode, boolean dependencyCheck) throws Exception;


    Object autowire(Class<?> beanClass, int autowireMode, boolean dependencyCheck) throws Exception;


    void autowireBeanProperties(Object existingBean, int autowireMode, boolean dependencyCheck)
            throws Exception;


    void applyBeanPropertyValues(Object existingBean, String beanName) throws Exception;


    Object initializeBean(Object existingBean, String beanName, GenericBeanDefinition mbd);


    Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName)
            throws Exception;


    Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName)
            throws Exception;


    void destroyBean(Object existingBean);


    Object resolveDependency( String beanName,
                             Set<String> autowiredBeanNames) throws Exception;

    Object resolveDependency(DependencyDescriptor descriptor, String beanName,
                             Set<String> autowiredBeanNames, TypeConverter typeConverter);


}
