package org.smileframework.ioc.bean.context.postprocessor;

/**
 * 提供初始化前后处理器
 * @author liuxin
 * @version Id: BeanPostProcessor.java, v 0.1 2018/10/18 8:11 PM
 */
public interface BeanPostProcessor {

    /**
     * 实例化前执行
     * @param bean 应该是Bean的Class
     * @param beanName
     * @return
     * @throws Exception
     */
    Object postProcessBeforeInitialization(Object bean, String beanName);

    /**
     *
     * @param existingBean 经过了前处理器，此时existingBean就是已经存在的Bean了
     * @param beanName
     * @return
     * @throws Exception
     */
    Object postProcessAfterInitialization(Object existingBean, String beanName) ;
}
