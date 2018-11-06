package org.smileframework.ioc.bean.context.parse;

import org.smileframework.ioc.bean.context.beandefinition.BeanDefinition;
import org.smileframework.ioc.bean.context.beanfactory.impl.DefaultListableBeanFactory;

/**
 * @author liuxin
 * @version Id: CustomerBeanDefinitionParse.java, v 0.1 2018/10/11 5:25 PM
 */
public abstract class CustomerBeanDefinitionParse implements BeanDefinitionParse {

    /**
     *
     * @param beanFactory
     */
    @Override
    public void loadBeanDefinitionParse(DefaultListableBeanFactory beanFactory) {

    }

    /**
     * 处理用户自定义的标记注解
     *
     * @return
     */
    abstract BeanDefinition doCustomerBeanDefinitionParse(Class beanCls);
}
