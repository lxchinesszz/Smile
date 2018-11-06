package org.smileframework.ioc.bean.context.postprocessor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smileframework.ioc.bean.context.SmileAnnotationApplicationContext;
import org.smileframework.ioc.bean.context.beandefinition.GenericBeanDefinition;
import org.smileframework.ioc.bean.context.beanfactory.exception.BeanCreationException;
import org.smileframework.ioc.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author liuxin
 * @version Id: InitDestroyAnnotationBeanPostProcessor.java, v 0.1 2018/10/18 10:49 PM
 */
public class InitDestroyAnnotationBeanPostProcessor implements DestructionAwareBeanPostProcessor {

    private Logger logger = LoggerFactory.getLogger(SmileAnnotationApplicationContext.class);

    private Class<? extends Annotation> initAnnotationType;

    private Class<? extends Annotation> destroyAnnotationType;

    /**
     * 用来缓存通过扫描注解构建出来的声明周期元数据LifecycleMetadata
     */
    private transient final Map<Class<?>, LifecycleMetadata> lifecycleMetadataCache =
            new ConcurrentHashMap<Class<?>, LifecycleMetadata>(64);

    public void setInitAnnotationType(Class<? extends Annotation> initAnnotationType) {
        this.initAnnotationType = initAnnotationType;
    }


    public void setDestroyAnnotationType(Class<? extends Annotation> destroyAnnotationType) {
        this.destroyAnnotationType = destroyAnnotationType;
    }

    /**
     * 销毁方法
     * @param bean
     * @param beanName
     * @throws Exception
     */
    @Override
    public void postProcessBeforeDestruction(Object bean, String beanName) throws Exception {
        //在销毁时候执行方法
        LifecycleMetadata metadata = findLifecycleMetadata(bean.getClass());
        try {
            metadata.invokeDestroyMethods(bean, beanName);
        }
        catch (InvocationTargetException ex) {
            String msg = "Invocation of destroy method failed on bean with name '" + beanName + "'";
            if (logger.isDebugEnabled()) {
                logger.warn(msg, ex.getTargetException());
            }
            else {
                logger.warn(msg + ": " + ex.getTargetException());
            }
        }
        catch (Throwable ex) {
            logger.error("Couldn't invoke destroy method on bean with name '" + beanName + "'", ex);
        }
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        //执行初始化方法
        LifecycleMetadata metadata = findLifecycleMetadata(bean.getClass());
        try {
            metadata.invokeInitMethods(bean, beanName);
        }
        catch (InvocationTargetException ex) {
            throw new BeanCreationException(beanName, "Invocation of init method failed", ex.getTargetException());
        }
        catch (Throwable ex) {
            throw new BeanCreationException(beanName, "Couldn't invoke init method", ex);
        }
        return bean;
    }

    /**
     * 因为本类主要是为了实现生命周期方法的
     * 所以只对初始化方法和销毁方法做处理,而初始化后不做任何处理
     * @param existingBean 经过了前处理器，此时existingBean就是已经存在的Bean了
     * @param beanName
     * @return
     */
    @Override
    public Object postProcessAfterInitialization(Object existingBean, String beanName) {
        return existingBean;
    }


    /***
     * buildLifecycleMetadata方法提供构建，但是不缓存，由findLifecycleMetadata来实现缓存实现
     * @param clazz
     * @return
     */
    private LifecycleMetadata findLifecycleMetadata(Class<?> clazz) {
        if (this.lifecycleMetadataCache == null) {
            // Happens after deserialization, during destruction...
            return buildLifecycleMetadata(clazz);
        }
        // Quick check on the concurrent map first, with minimal locking.
        LifecycleMetadata metadata = this.lifecycleMetadataCache.get(clazz);
        if (metadata == null) {
            synchronized (this.lifecycleMetadataCache) {
                metadata = this.lifecycleMetadataCache.get(clazz);
                if (metadata == null) {
                    metadata = buildLifecycleMetadata(clazz);
                    this.lifecycleMetadataCache.put(clazz, metadata);
                }
                return metadata;
            }
        }
        return metadata;
    }

    /**
     * 从注解中构建声明周期方法
     * 从当前扫描的Class中通过遍历所有的方法，找到带有PostConstruct初始化和PreDestroy销毁的方法并生成生命周期类
     * 具体实现可看InitDestroyAnnotationBeanPostProcessor的实现类
     * @see CommonAnnotationBeanPostProcessor
     * @see org.smileframework.ioc.bean.annotation.PostConstruct
     * @see org.smileframework.ioc.bean.annotation.PreDestroy
     * @param clazz
     * @return
     */
    private LifecycleMetadata buildLifecycleMetadata(Class<?> clazz) {
        final boolean debug = logger.isDebugEnabled();
        LinkedList<LifecycleElement> initMethods = new LinkedList<LifecycleElement>();
        LinkedList<LifecycleElement> destroyMethods = new LinkedList<LifecycleElement>();
        Class<?> targetClass = clazz;
        do {
            LinkedList<LifecycleElement> currInitMethods = new LinkedList<LifecycleElement>();
            LinkedList<LifecycleElement> currDestroyMethods = new LinkedList<LifecycleElement>();
            for (Method method : targetClass.getDeclaredMethods()) {
                //从所有的方法中获取带有初始化注解的，Spring中是扫描的@PostConstruct
                if (this.initAnnotationType != null) {
                    if (method.getAnnotation(this.initAnnotationType) != null) {
                        LifecycleElement element = new LifecycleElement(method);
                        currInitMethods.add(element);
                        if (debug) {
                            logger.debug("Found init method on class [" + clazz.getName() + "]: " + method);
                        }
                    }
                }
                //从所有的方法中获取带有销毁的注解，Spring中是扫描的@PreDestroy
                if (this.destroyAnnotationType != null) {
                    if (method.getAnnotation(this.destroyAnnotationType) != null) {
                        currDestroyMethods.add(new LifecycleElement(method));
                        if (debug) {
                            logger.debug("Found destroy method on class [" + clazz.getName() + "]: " + method);
                        }
                    }
                }
            }
            initMethods.addAll(0, currInitMethods);
            destroyMethods.addAll(currDestroyMethods);
            targetClass = targetClass.getSuperclass();
        }
        while (targetClass != null && targetClass != Object.class);

        return new LifecycleMetadata(clazz, initMethods, destroyMethods);
    }
    /**
     * 声明周期元数据
     * 主要保存对PostConstruct注解修饰的初始化方法和PreDestroy销毁前方法
     */
    private class LifecycleMetadata {

        private final Class<?> targetClass;

        private final Collection<LifecycleElement> initMethods;

        private final Collection<LifecycleElement> destroyMethods;

        private volatile Set<LifecycleElement> checkedInitMethods;

        private volatile Set<LifecycleElement> checkedDestroyMethods;

        public LifecycleMetadata(Class<?> targetClass, Collection<LifecycleElement> initMethods,
                                 Collection<LifecycleElement> destroyMethods) {

            this.targetClass = targetClass;
            this.initMethods = initMethods;
            this.destroyMethods = destroyMethods;
        }

        public void checkConfigMembers(GenericBeanDefinition beanDefinition) {
            Set<LifecycleElement> checkedInitMethods = new LinkedHashSet<LifecycleElement>(this.initMethods.size());
            for (LifecycleElement element : this.initMethods) {
                String methodIdentifier = element.getIdentifier();
                checkedInitMethods.add(element);
                if (logger.isDebugEnabled()) {
                    logger.debug("Registered init method on class [" + this.targetClass.getName() + "]: " + element);
                }

            }
            Set<LifecycleElement> checkedDestroyMethods = new LinkedHashSet<LifecycleElement>(this.destroyMethods.size());
            for (LifecycleElement element : this.destroyMethods) {
                String methodIdentifier = element.getIdentifier();
                checkedDestroyMethods.add(element);
                if (logger.isDebugEnabled()) {
                    logger.debug("Registered destroy method on class [" + this.targetClass.getName() + "]: " + element);
                }
            }
            this.checkedInitMethods = checkedInitMethods;
            this.checkedDestroyMethods = checkedDestroyMethods;
        }

        public void invokeInitMethods(Object target, String beanName) throws Throwable {
            Collection<LifecycleElement> initMethodsToIterate =
                    (this.checkedInitMethods != null ? this.checkedInitMethods : this.initMethods);
            if (!initMethodsToIterate.isEmpty()) {
                boolean debug = logger.isDebugEnabled();
                for (LifecycleElement element : initMethodsToIterate) {
                    if (debug) {
                        logger.debug("Invoking init method on bean '" + beanName + "': " + element.getMethod());
                    }
                    element.invoke(target);
                }
            }
        }

        public void invokeDestroyMethods(Object target, String beanName) throws Throwable {
            Collection<LifecycleElement> destroyMethodsToIterate =
                    (this.checkedDestroyMethods != null ? this.checkedDestroyMethods : this.destroyMethods);
            if (!destroyMethodsToIterate.isEmpty()) {
                boolean debug = logger.isDebugEnabled();
                for (LifecycleElement element : destroyMethodsToIterate) {
                    if (debug) {
                        logger.debug("Invoking destroy method on bean '" + beanName + "': " + element.getMethod());
                    }
                    element.invoke(target);
                }
            }
        }
    }


    private static class LifecycleElement {

        private final Method method;

        private final String identifier;

        /**
         * 声明周期方法,不允许有参数
         *
         * @param method
         */
        public LifecycleElement(Method method) {
            if (method.getParameterTypes().length != 0) {
                throw new IllegalStateException("Lifecycle method annotation requires a no-arg method: " + method);
            }
            this.method = method;
            this.identifier = (Modifier.isPrivate(method.getModifiers()) ?
                    method.getDeclaringClass() + "." + method.getName() : method.getName());
        }

        public Method getMethod() {
            return this.method;
        }

        public String getIdentifier() {
            return this.identifier;
        }

        public void invoke(Object target) throws Throwable {
            ReflectionUtils.makeAccessible(this.method);
            this.method.invoke(target, (Object[]) null);
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof LifecycleElement)) {
                return false;
            }
            LifecycleElement otherElement = (LifecycleElement) other;
            return (this.identifier.equals(otherElement.identifier));
        }

        @Override
        public int hashCode() {
            return this.identifier.hashCode();
        }
    }

}
