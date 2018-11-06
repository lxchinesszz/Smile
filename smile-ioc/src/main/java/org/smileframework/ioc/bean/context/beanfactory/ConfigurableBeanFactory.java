package org.smileframework.ioc.bean.context.beanfactory;

import org.smileframework.ioc.bean.context.postprocessor.BeanPostProcessor;

/**
 * @author liuxin
 * @version Id: ConfigurableBeanFactory.java, v 0.1 2018/10/12 6:29 PM
 */
public interface ConfigurableBeanFactory extends SingletonBeanRegistry{
    /**
     * Scope identifier for the standard singleton scope: "singleton".
     * Custom scopes can be added via {@code registerScope}.
     */
    String SCOPE_SINGLETON = "singleton";

    /**
     * Scope identifier for the standard prototype scope: "prototype".
     * Custom scopes can be added via {@code registerScope}.
     */
    String SCOPE_PROTOTYPE = "prototype";

    /**
     * 添加前后处理器
     * @param beanPostProcessor
     */
    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);

    /**
     * 获取处理器个数
     * @return
     */
    int getBeanPostProcessorCount();


    /**
     * 判断是否是FactoryBean
     * @param name
     * @return
     */
    boolean isFactoryBean(String name);

    /**
     * 判断是否循环依赖
     * @param beanName
     * @return
     */
    boolean isCurrentlyInCreation(String beanName);

}
