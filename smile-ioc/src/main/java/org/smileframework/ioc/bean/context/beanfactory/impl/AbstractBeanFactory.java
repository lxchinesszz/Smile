package org.smileframework.ioc.bean.context.beanfactory.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smileframework.ioc.bean.context.SmileAnnotationApplicationContext;
import org.smileframework.ioc.bean.context.beandefinition.GenericBeanDefinition;
import org.smileframework.ioc.bean.context.beanfactory.BeanFactory;
import org.smileframework.ioc.bean.context.beanfactory.BeanFactoryUtils;
import org.smileframework.ioc.bean.context.beanfactory.ConfigurableBeanFactory;
import org.smileframework.ioc.bean.context.beanfactory.exception.BeanCurrentlyInCreationException;
import org.smileframework.ioc.bean.context.factorybean.FactoryBean;
import org.smileframework.ioc.bean.context.postprocessor.BeanPostProcessor;
import org.smileframework.ioc.bean.context.postprocessor.DestructionAwareBeanPostProcessor;
import org.smileframework.ioc.bean.context.postprocessor.InstantiationAwareBeanPostProcessor;
import org.smileframework.tool.asserts.Assert;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 通用方法
 *
 * @author liuxin
 * @version Id: AbstractBeanFactory.java, v 0.1 2018/10/29 11:39 AM
 */
public abstract class AbstractBeanFactory implements ConfigurableBeanFactory,BeanFactory {

    private Logger logger = LoggerFactory.getLogger(AbstractBeanFactory.class);
    /**
     *
     */
    private final Set<BeanPostProcessor> beanPostProcessors = new HashSet<>();

    protected static final Object NULL_OBJECT = new Object();

    /**
     * 循环依赖标记
     * 如果当前类A正在创建,依赖的类B也在创建,当创建B时候又创建A，此时就构成循环依赖
     * A--->B--->A 循环依赖
     */
    private final Set<String> singletonsCurrentlyInCreation =
            Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>(16));

    private final Set<String> inCreationCheckExclusions =
            Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>(16));


    /** Cache of singleton objects: bean name --> bean instance */
    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<String, Object>(64);

    /** Cache of singleton factories: bean name --> ObjectFactory */
    private final Map<String, ObjectFactory<?>> singletonFactories = new HashMap<String, ObjectFactory<?>>(16);

    /**
     * 缓存FactoryBean的接口
     * */
    private final Map<String, Object> factoryBeanObjectCache = new ConcurrentHashMap<String, Object>(16);

    /**
     * 如果实现了实例化前后处理器在改标识为true
     * 如果为true就以为这有部分的Bean会是由处理器来直接生成实例，而不是都依赖于Spring来构建
     */
    private boolean hasInstantiationAwareBeanPostProcessors;

    /** Indicates whether any DestructionAwareBeanPostProcessors have been registered */
    private boolean hasDestructionAwareBeanPostProcessors;
    /**
     * 返回所有的处理器
     * @return
     */
    public List<BeanPostProcessor> getBeanPostProcessors() {
        return new ArrayList<>(this.beanPostProcessors);
    }

    @Override
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        Assert.notNull(beanPostProcessor, "BeanPostProcessor must not be null");
        this.beanPostProcessors.add(beanPostProcessor);
        if (beanPostProcessor instanceof InstantiationAwareBeanPostProcessor) {
            this.hasInstantiationAwareBeanPostProcessors = true;
        }
        if (beanPostProcessor instanceof DestructionAwareBeanPostProcessor) {
            this.hasDestructionAwareBeanPostProcessors = true;
        }
    }

    @Override
    public int getBeanPostProcessorCount() {
        return beanPostProcessors.size();
    }



    @Override
    public boolean isCurrentlyInCreation(String beanName) {
        return false;
    }

    @Override
    public void registerSingleton(String beanName, Object singletonObject) {

    }

    @Override
    public Object getSingleton(String beanName) {
        Object singletonObject = this.singletonObjects.get(beanName);
        return singletonObject;
    }

    /**
     * 对名字先进行处理,如果是FactoryBean的名字就把名字转换成Bean的名字
     * 先从单例缓存中
     * @param name Bean名字
     * @param requiredType 将要获取的Bean类型
     * @param args 参数
     * @param typeCheckOnly 是否检查类型
     * @param <T>
     * @return
     */
    protected <T> T doGetBean(
            final String name, final Class<T> requiredType, final Object[] args, boolean typeCheckOnly) {
        //获取转换后的BeanName,如果是FactoryBean就把前缀&去掉
        final String beanName = transformedBeanName(name);
        Object bean;

        //从单例中获取实例
        Object sharedInstance = getSingleton(beanName);

        if (sharedInstance != null && args == null) {
            if (logger.isDebugEnabled()) {
                if (isSingletonCurrentlyInCreation(beanName)) {
                    logger.debug("Returning eagerly cached instance of singleton bean '" + beanName +
                            "' that is not fully initialized yet - a consequence of a circular reference");
                }
                else {
                    logger.debug("Returning cached instance of singleton bean '" + beanName + "'");
                }
            }
            //先判断是否是FactoryBean,如果是调用getObject()
            bean = getObjectForBeanInstance(sharedInstance, beanName);
        }else {
            bean = containsBean(beanName);
        }
        return (T)bean;
    }


    /**
     * 创建Bean,因为有很多的依赖所有智能有
     * @param beanName
     * @param mbd
     * @param args
     * @return
     */
    protected abstract Object createBean(String beanName, GenericBeanDefinition mbd, Object[] args);


    /**
     * 从FactoryBean中getObject获取实例
     * @param beanInstance
     * @param beanName
     * @return
     */
    protected Object getObjectForBeanInstance(
            Object beanInstance, String beanName){
        //如果不是FactoryBean,但是还是以&开头,就报错.
        if (BeanFactoryUtils.isFactoryDereference(beanName) && !(beanInstance instanceof FactoryBean)) {
            System.err.println("以&开头,但是不是FactoryBean的实例,直接报错");
        }
        //如果是&开头,但是不是FactoryBean就直接返回
        if (!(beanInstance instanceof FactoryBean) || BeanFactoryUtils.isFactoryDereference(beanName)) {
            return beanInstance;
        }
        Object object  = getCachedObjectForFactoryBean(beanName);
        if (object == null) {
            // Return bean instance from factory.
            FactoryBean<?> factory = (FactoryBean<?>) beanInstance;
            // Caches object obtained from FactoryBean if it is a singleton.
            /**
             * 如果是单例就把他放到缓存中
             */
            if (factory.isSingleton()){
                object = factory.getObject();
                this.factoryBeanObjectCache.put(beanName, (object != null ? object : NULL_OBJECT));
            }
        }
        return object;
    }

    /**
     * 先从缓存中拿到FactoryBean的Bean
     * @param beanName
     * @return
     */
    protected Object getCachedObjectForFactoryBean(String beanName) {
        Object object = this.factoryBeanObjectCache.get(beanName);
        return object ;
    }
    /**
     * Return whether the specified singleton bean is currently in creation
     * (within the entire factory).
     * @param beanName the name of the bean
     */
    public boolean isSingletonCurrentlyInCreation(String beanName) {
        return this.singletonsCurrentlyInCreation.contains(beanName);
    }

    /**
     * Callback before singleton creation.
     * <p>The default implementation register the singleton as currently in creation.
     * @param beanName the name of the singleton about to be created
     * @see #isSingletonCurrentlyInCreation
     */
    protected void beforeSingletonCreation(String beanName) {
        if (!this.inCreationCheckExclusions.contains(beanName) &&
                !this.singletonsCurrentlyInCreation.add(beanName)) {
            throw new BeanCurrentlyInCreationException(beanName);
        }
    }

    /**
     * Callback after singleton creation.
     * <p>The default implementation marks the singleton as not in creation anymore.
     * @param beanName the name of the singleton that has been created
     * @see #isSingletonCurrentlyInCreation
     */
    protected void afterSingletonCreation(String beanName) {
        if (!this.inCreationCheckExclusions.contains(beanName) &&
                !this.singletonsCurrentlyInCreation.remove(beanName)) {
            throw new IllegalStateException("Singleton '" + beanName + "' isn't currently in creation");
        }
    }


    /**
     * 转换后的BeanName
     * @param name
     * @return
     */
    protected String transformedBeanName(String name) {
        return (BeanFactoryUtils.transformedBeanName(name));
    }

    @Override
    public Object getBean(String name) {
        return null;
    }

    @Override
    public <T> T getBean(Class<T> requiredType) {
        return null;
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) {
        return null;
    }

    @Override
    public boolean containsBean(String name) {
        return false;
    }

    @Override
    public boolean isSingleton(String name) {
        return false;
    }

    @Override
    public boolean isPrototype(String name) {
        return false;
    }

    @Override
    public Class getType(String name) {
        return null;
    }
}
