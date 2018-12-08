package org.smileframework.ioc.bean.context.beanfactory.impl;

import com.google.common.base.Throwables;
import org.smileframework.ioc.bean.annotation.Autowired;
import org.smileframework.ioc.bean.annotation.SmileBean;
import org.smileframework.ioc.bean.annotation.Value;
import org.smileframework.ioc.bean.context.beandefinition.ConstructorArgumentValue;
import org.smileframework.ioc.bean.context.beandefinition.ConstructorArgumentValues;
import org.smileframework.ioc.bean.context.beandefinition.ConstructorInfo;
import org.smileframework.ioc.bean.context.beandefinition.GenericBeanDefinition;
import org.smileframework.ioc.bean.context.beanfactory.AutowireCapableBeanFactory;
import org.smileframework.ioc.bean.context.beanfactory.InitializingBean;
import org.smileframework.ioc.bean.context.beanfactory.exception.BeanCreationException;
import org.smileframework.ioc.bean.context.postprocessor.BeanPostProcessor;
import org.smileframework.ioc.bean.context.postprocessor.InstantiationAwareBeanPostProcessor;
import org.smileframework.tool.annotation.AnnotationTools;
import org.smileframework.tool.clazz.ClassTools;
import org.smileframework.tool.clazz.MethodTools;
import org.smileframework.tool.clazz.ReflectionTools;
import org.smileframework.tool.string.StringTools;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 主要操作Bean的生成
 *
 * @author liuxin
 * @version Id: AbstractAutowireCapableBeanFactory.java, v 0.1 2018/10/18 8:41 PM
 */
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory implements AutowireCapableBeanFactory {

    /**
     * 如果实例化处理器返回了Bean,就直接返回，否则创建
     * <p>
     * 否则就用IOC实例->然后执行实例化后置处理器->填充->执行初始化前后处理器和初始化方法
     *
     * @param beanName
     * @param mbd
     * @param args
     * @return
     */
    @Override
    protected Object createBean(String beanName, GenericBeanDefinition mbd, Object[] args) {
        //先从处理器中获取，一般创建代理对象会从这里面获取，主要是继承InstantiationAwareBeanPostProcessor
        //重写实例前置处理器applyBeanPostProcessorsBeforeInstantiation方法，如果获取成功就执行初始化后方法(postProcessAfterInitialization)
        Object bean = resolveBeforeInstantiation(beanName, mbd);
        if (bean != null) {
            return bean;
        }
        /**
         * 没有在执行创建
         */
        Object beanInstance = doCreateBean(beanName, mbd, args);
        return beanInstance;
    }

    /**
     * 执行注入
     *
     * @param beanName
     * @param mbd
     * @param bean
     */
    protected void populateBean(String beanName, GenericBeanDefinition mbd, Object bean) {
        for (BeanPostProcessor bp : getBeanPostProcessors()) {
            if (bp instanceof InstantiationAwareBeanPostProcessor) {
                InstantiationAwareBeanPostProcessor ibp = (InstantiationAwareBeanPostProcessor) bp;
                if (!ibp.postProcessAfterInstantiation(bean, beanName)) {
                    break;
                }
            }
        }
        //填充
        for (BeanPostProcessor bp : getBeanPostProcessors()) {
            if (bp instanceof InstantiationAwareBeanPostProcessor) {
                InstantiationAwareBeanPostProcessor ibp = (InstantiationAwareBeanPostProcessor) bp;
                ibp.postProcessPropertyValues(bean, beanName);
            }
        }
    }

    protected Object doCreateBean(final String beanName, final GenericBeanDefinition mbd, final Object[] args) {
        //首先实例
        Object instanceBean = createBeanInstance(beanName, mbd, args);
        //然后填充
        this.populateBean(beanName, mbd, instanceBean);
        //执行初始化
        Object exposedObject = initializeBean(instanceBean, beanName, mbd);
        return exposedObject;
    }

    /**
     * 当有空构造可以先实例
     */
    protected Object createBeanInstance(final String beanName, final GenericBeanDefinition mbd, final Object[] args) {
        Object bean = null;
        boolean isAutowireConstructor = false;
        Class beanClass = mbd.getBeanClass();
        if (Modifier.isInterface(beanClass.getModifiers())) {
            throw new BeanCreationException(beanName, "接口不能被实例化");
        }
        //如果无参构造直接实例
        List<ConstructorInfo> constructorInfoList = mbd.getConstructorInfo();
        for (ConstructorInfo constructorInfo : constructorInfoList) {
            List<Annotation> declaredAnnotations = constructorInfo.getDeclaredAnnotations();
            isAutowireConstructor = AnnotationTools.isContainsAnnotation(declaredAnnotations, Autowired.class);
            if (isAutowireConstructor) {
                break;
            }
        }
        //如果有空构造并没有构造注入就使用空构造实例
        if (mbd.isEmptyConstructorFlag() && !isAutowireConstructor) {
            try {
                Constructor constructor = mbd.getBeanClass().getDeclaredConstructor();
                ReflectionTools.makeAccessible(constructor);
                bean = constructor.newInstance();
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        } else {
            //使用构造注入
            bean = autowireConstructor(beanName, mbd);
        }
        return bean;
    }


    /**
     * 根据构造实例化Bean。
     * 什么时候回用到?
     *
     * 当一个bean有空构造时候,此时实例化可以直接进行实例化操作constructor.newInstance();
     * 但是当一个Bean要根据构造注入时候,这个时候就不能进行直接实例化,因为构造中的参数要从IOC容器中获取
     *
     *
     * @param beanName 将要实例化的BeanName
     * @param mbd Bean描述信息
     * @return 实例化的bean
     */
    private Object autowireConstructor(final String beanName, final GenericBeanDefinition mbd) {
        Object instance = null;
        Object[] args = null;
        Constructor constructorInvoke = null;
        //如果无参构造直接实例
        List<ConstructorInfo> constructorInfoList = mbd.getConstructorInfo();
        for (ConstructorInfo constructorInfo : constructorInfoList) {
            List<Annotation> declaredAnnotations = constructorInfo.getDeclaredAnnotations();
            boolean isAutowireConstructor = AnnotationTools.isContainsAnnotation(declaredAnnotations, Autowired.class);
            if (isAutowireConstructor) {
                ConstructorArgumentValues constructorArgumentList = constructorInfo.getConstructorArgumentValues();
                List<ConstructorArgumentValue> constructorArguments = constructorArgumentList.getConstructorArgumentValues();
                args = new Object[constructorArguments.size()];
                for (ConstructorArgumentValue constructorArgumentValue : constructorArguments) {
                    int sort = constructorArgumentValue.getSort();
                    Value valueAnnotation = AnnotationTools.findAnnotation(constructorArgumentValue.getAnnotations(), Value.class);
                    if (null != valueAnnotation) {
                        String value = getPropertyResolver().resolvePlaceholders(valueAnnotation.value());
                        args[sort] = getTypeConverter().convertIfNecessary(value,constructorArgumentValue.getCls());
                    } else {
                        String dependencyBeanName = beanNameGenerator.generateBeanName(constructorArgumentValue.getCls());
                        args[sort] = resolveDependency(dependencyBeanName);
                    }
                }
                constructorInvoke = constructorInfo.getOriginalConstructor();
                break;
            }
        }
        try {
            if (constructorInvoke != null) {
                instance = constructorInvoke.newInstance(args);
            }
        } catch (Exception e) {
            System.err.println("构造注入异常: " + Throwables.getStackTraceAsString(e));
        }
        return instance;
    }


    /**
     * 解决实例前的问题
     *
     * @param beanName
     * @param mbd
     * @return
     */
    protected Object resolveBeforeInstantiation(String beanName, GenericBeanDefinition mbd) {
        Object bean = null;
        if (!Boolean.FALSE.equals(mbd.beforeInstantiationResolved)) {
            bean = applyBeanPostProcessorsBeforeInstantiation(mbd.getBeanClass(), beanName);
            if (bean != null) {
                bean = applyBeanPostProcessorsAfterInitialization(bean, beanName);
            }
            mbd.beforeInstantiationResolved = (bean != null);
        }
        return bean;
    }

    /**
     * 应用所有的实例化处理器
     *
     * @param beanClass
     * @param beanName
     * @return
     */
    protected Object applyBeanPostProcessorsBeforeInstantiation(Class<?> beanClass, String beanName) {

        for (BeanPostProcessor bp : getBeanPostProcessors()) {
            if (bp instanceof InstantiationAwareBeanPostProcessor) {
                InstantiationAwareBeanPostProcessor ibp = (InstantiationAwareBeanPostProcessor) bp;
                Object result = ibp.postProcessBeforeInstantiation(beanClass, beanName);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }

    @Override
    public <T> T createBean(Class<T> beanClass) throws Exception {
        return null;
    }

    @Override
    public void autowireBean(Object existingBean) throws Exception {

    }

    @Override
    public Object configureBean(Object existingBean, String beanName) throws Exception {
        return null;
    }

    @Override
    public Object resolveDependency(String beanName) {
        Object bean = getBean(beanName);
        if (null ==bean){
            throw new BeanCreationException(beanName,"未查询到bean");
        }
        return bean;
    }

    @Override
    public Object createBean(Class<?> beanClass, int autowireMode, boolean dependencyCheck) throws Exception {
        return null;
    }

    @Override
    public Object autowire(Class<?> beanClass, int autowireMode, boolean dependencyCheck) throws Exception {
        return null;
    }

    @Override
    public void autowireBeanProperties(Object existingBean, int autowireMode, boolean dependencyCheck) throws Exception {

    }

    @Override
    public void applyBeanPropertyValues(Object existingBean, String beanName) throws Exception {

    }

    @Override
    public Object initializeBean(Object existingBean, String beanName, GenericBeanDefinition mbd) {

        //执行初始化前方法
        existingBean = applyBeanPostProcessorsBeforeInitialization(existingBean, beanName);
        //执行初始化方法
        invokeInitMethods(existingBean, mbd);
        //执行初始化后方法
        existingBean = applyBeanPostProcessorsAfterInitialization(existingBean, beanName);

        return existingBean;
    }

    protected void invokeInitMethods(Object bean, GenericBeanDefinition mbd) {
        boolean isInitializingBean = bean instanceof InitializingBean;
        if (isInitializingBean) {
            ((InitializingBean) bean).afterPropertiesSet();
        }

        //执行用户自定义的错误
        if (StringTools.isNotEmpty(mbd.getInitMethodName())) {
            invokeCustomInitMethod(bean, mbd);
        }
    }

    /**
     * 执行自定义的初始化方法
     */
    protected void invokeCustomInitMethod(Object bean, GenericBeanDefinition mbd) {
        String initMethodName = mbd.getInitMethodName();
        Method initMethodInvoke = MethodTools.findMethodByName(mbd.getBeanClass(), initMethodName, null);
        ReflectionTools.makeAccessible(initMethodInvoke);
        try {
            initMethodInvoke.invoke(bean);
        } catch (Exception in) {
            System.err.println(in.getMessage());
        }
    }

    /**
     * 初始化前
     *
     * @param existingBean
     * @param beanName
     * @return
     */
    @Override
    public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) {
        Object result = existingBean;
        for (BeanPostProcessor beanProcessor : getBeanPostProcessors()) {
            result = beanProcessor.postProcessBeforeInitialization(result, beanName);
            if (result == null) {
                return result;
            }
        }
        return result;
    }

    /**
     * 初始化后
     *
     * @param existingBean
     * @param beanName
     * @return
     */
    @Override
    public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) {
        Object result = existingBean;
        for (BeanPostProcessor beanProcessor : getBeanPostProcessors()) {
            result = beanProcessor.postProcessAfterInitialization(result, beanName);
            if (result == null) {
                return result;
            }
        }
        return result;
    }

    @Override
    public void destroyBean(Object existingBean) {

    }

    @Override
    public Object resolveDependency(String beanName, Set<String> autowiredBeanNames) throws Exception {
        return null;
    }
}
