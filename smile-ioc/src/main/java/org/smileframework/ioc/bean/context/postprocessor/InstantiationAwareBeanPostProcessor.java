package org.smileframework.ioc.bean.context.postprocessor;

/**
 * 提供实例化前后处理器
 *
 * @author liuxin
 * @version Id: InstantiationAwareBeanPostProcessor.java, v 0.1 2018/10/18 9:15 PM
 */
public interface InstantiationAwareBeanPostProcessor extends BeanPostProcessor {


    /**
     * 前实例化接口
     *
     * @param beanClass
     * @param beanName
     * @return
     * @throws Exception
     */
    Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName);

    /**
     * 后实例化
     *
     * @param bean
     * @param beanName
     * @return
     * @throws Exception
     */
    boolean postProcessAfterInstantiation(Object bean, String beanName);


    /**
     * 对bean的属性值进行注入
     *
     * @param bean     实例化的Bean(所有的类都是经过实例然后在进行属性注入填充)
     * @param beanName 实例化的BeanName
     */
    void postProcessPropertyValues(Object bean, String beanName);
}
