package org.smileframework.ioc.bean.context.postprocessor;

/**
 * 提供销毁前处理器
 * @author liuxin
 * @version Id: DestructionAwareBeanPostProcessor.java, v 0.1 2018/10/18 10:49 PM
 */
public interface DestructionAwareBeanPostProcessor extends BeanPostProcessor{
    void postProcessBeforeDestruction(Object bean, String beanName) throws Exception;
}
