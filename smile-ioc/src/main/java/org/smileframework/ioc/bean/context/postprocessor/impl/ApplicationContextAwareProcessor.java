package org.smileframework.ioc.bean.context.postprocessor.impl;

import org.smileframework.ioc.bean.context.AbstractSmileApplicationContext;
import org.smileframework.ioc.bean.context.ConfigurableApplicationContext;
import org.smileframework.ioc.bean.context.aware.ApplicationContextAware;
import org.smileframework.ioc.bean.context.aware.Aware;
import org.smileframework.ioc.bean.context.aware.BeanFactoryAware;
import org.smileframework.ioc.bean.context.aware.EnvironmentAware;
import org.smileframework.ioc.bean.context.beanfactory.BeanFactory;
import org.smileframework.ioc.bean.context.postprocessor.BeanPostProcessor;

/**
 * ApplicationContextAwareProcessor
 * 应用上下文处理器只有一个目的就是对所有继承Aware接口的Bean,进行注入Aware对象
 *
 * @author liuxin
 * @version Id: ApplicationContextAwareProcessor.java, v 0.1 2018/10/29 11:32 AM
 */
public class ApplicationContextAwareProcessor implements BeanPostProcessor {

    private AbstractSmileApplicationContext applicationContext;

    /**
     * Create a new ApplicationContextAwareProcessor for the given context.
     */
    public ApplicationContextAwareProcessor(AbstractSmileApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * 在里面对实现Aware接口进行注册
     *
     * @param bean     应该是Bean的Class
     * @param beanName
     * @return
     * @throws Exception
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        if (bean != null && (bean instanceof ApplicationContextAware || bean instanceof EnvironmentAware)) {
            invokeAwareInterfaces(bean);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object existingBean, String beanName) {
        return existingBean;
    }


    private void invokeAwareInterfaces(Object bean) {
        if (bean instanceof Aware) {
            if (bean instanceof EnvironmentAware) {
                ((EnvironmentAware) bean).setEnvironment(this.applicationContext.getEnvironment());
            }
            if (bean instanceof ApplicationContextAware) {
                ((ApplicationContextAware) bean).setApplicationContext(this.applicationContext);
            }
        }
    }
}
