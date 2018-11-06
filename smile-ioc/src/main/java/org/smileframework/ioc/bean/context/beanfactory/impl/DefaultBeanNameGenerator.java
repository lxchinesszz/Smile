package org.smileframework.ioc.bean.context.beanfactory.impl;

import org.smileframework.ioc.bean.context.beandefinition.BeanDefinition;
import org.smileframework.ioc.bean.context.beandefinition.GenericBeanDefinition;
import org.smileframework.ioc.bean.context.beanfactory.BeanFactory;
import org.smileframework.ioc.bean.context.beanfactory.BeanNameGenerator;
import org.smileframework.tool.string.StringTools;

/**
 * @author liuxin
 * @version Id: DefaultBeanNameGenerator.java, v 0.1 2018/10/26 6:15 PM
 */
public class DefaultBeanNameGenerator implements BeanNameGenerator {

    /**
     * 默认就是首字母小写
     * 如果是FactoryBean就在前面加$
     * @param definition
     * @return
     */
    @Override
    public String generateBeanName(BeanDefinition definition) {
        GenericBeanDefinition genericBeanDefinition = (GenericBeanDefinition) definition;
        boolean factoryBean = genericBeanDefinition.isFactoryBean();
        String name = factoryBean? BeanFactory.FACTORY_BEAN_PREFIX+ StringTools.uncapitalize(genericBeanDefinition.getBeanName()):genericBeanDefinition.getBeanName();
        return name;
    }

}
