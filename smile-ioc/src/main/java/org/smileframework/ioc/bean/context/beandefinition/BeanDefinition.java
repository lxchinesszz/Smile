package org.smileframework.ioc.bean.context.beandefinition;

import org.smileframework.ioc.bean.context.beanfactory.ConfigurableBeanFactory;

import java.util.List;

/**
 * @Package: pig.boot.ioc.context
 * @Description: bean描述
 * @author: liuxin
 * @date: 2017/11/17 下午11:53
 */
public interface BeanDefinition {

    String SCOPE_SINGLETON = ConfigurableBeanFactory.SCOPE_SINGLETON;

    String SCOPE_PROTOTYPE = ConfigurableBeanFactory.SCOPE_PROTOTYPE;

    /**
     * 字节码名字
     * @return
     */
    String getBeanClassName();

    /**
     * 获取Bean的域范围: 单例还是远星
     * @return
     */
    String getScope();

    /**
     * 设置
     * @param scope
     */
    void setScope(String scope);

    /**
     * 是否懒加载
     * @return
     */
    boolean isLazyInit();


    /**
     * 设置是否懒加载
     * @param lazyInit
     */
    void setLazyInit(boolean lazyInit);


    String[] getDependsOn();


    void setDependsOn(String[] dependsOn);

    /**
     * Return whether this a <b>Singleton</b>, with a single, shared instance
     * returned on all calls.
     * @see #SCOPE_SINGLETON
     */
    boolean isSingleton();

    /**
     * Return whether this a <b>Prototype</b>, with an independent instance
     * returned for each call.
     * @see #SCOPE_PROTOTYPE
     */
    boolean isPrototype();

    /**
     * Return whether this bean is "abstract", that is, not meant to be instantiated.
     */
    boolean isAbstract();

    /**
     * Return whether this bean is a primary autowire candidate.
     * If this value is true for exactly one bean among multiple
     * matching candidates, it will serve as a tie-breaker.
     */
    boolean isPrimary();

    /**
     * Set whether this bean is a primary autowire candidate.
     * <p>If this value is true for exactly one bean among multiple
     * matching candidates, it will serve as a tie-breaker.
     */
    void setPrimary(boolean primary);


    /**
     * Return whether this bean is a candidate for getting autowired into some other bean.
     */
    boolean isAutowireCandidate();

    String getBeanName();

    boolean isFactoryBean();

    List<ConstructorInfo> getConstructorInfo();


    Class getBeanClass();



}
