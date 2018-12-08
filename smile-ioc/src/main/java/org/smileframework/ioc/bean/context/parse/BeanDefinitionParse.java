package org.smileframework.ioc.bean.context.parse;

import org.smileframework.ioc.bean.context.beanfactory.impl.DefaultListableBeanFactory;

/**
 * @author liuxin
 * @version Id: BeanDefinitionParse.java, v 0.1 2018/10/26 5:42 PM
 */
public interface BeanDefinitionParse {

    /**
     * 加载解析到的BeanDefinition到BeanFactory
     * @param beanFactory bean工厂
     */
    void loadBeanDefinitionParse(DefaultListableBeanFactory beanFactory);

}
