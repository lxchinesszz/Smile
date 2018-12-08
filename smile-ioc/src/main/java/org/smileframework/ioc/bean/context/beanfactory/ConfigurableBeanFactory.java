package org.smileframework.ioc.bean.context.beanfactory;

import org.smileframework.ioc.bean.context.beandefinition.BeanDefinition;
import org.smileframework.ioc.bean.context.beanfactory.convert.TypeConverter;
import org.smileframework.ioc.bean.context.postprocessor.BeanPostProcessor;
import org.smileframework.ioc.bean.core.env.PropertyResolver;

import java.util.Map;

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

    /**
     * 可配置的配置信息
     * @param propertyResolver
     */
    void setPropertyResolver(PropertyResolver propertyResolver);

    /**
     * 指定转换类型
     * @param typeConverter
     * @return
     */
    TypeConverter setTypeConverter(TypeConverter typeConverter);


    Map<String, BeanDefinition> getBeanDefinitioin();

}
