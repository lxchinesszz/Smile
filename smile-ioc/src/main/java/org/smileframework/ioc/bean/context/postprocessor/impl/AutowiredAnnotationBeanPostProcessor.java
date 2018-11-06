package org.smileframework.ioc.bean.context.postprocessor.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.smileframework.ioc.bean.annotation.Autowired;
import org.smileframework.ioc.bean.annotation.InsertBean;
import org.smileframework.ioc.bean.annotation.Value;
import org.smileframework.ioc.bean.context.beanfactory.exception.BeanCreationException;
import org.smileframework.ioc.bean.context.postprocessor.InstantiationAwareBeanPostProcessor;
import org.smileframework.ioc.util.ReflectionUtils;
import org.smileframework.tool.annotation.AnnotationTools;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author liuxin
 * @version Id: AutowiredAnnotationBeanPostProcessor.java, v 0.1 2018/10/30 2:11 PM
 */
public class AutowiredAnnotationBeanPostProcessor implements InstantiationAwareBeanPostProcessor{
    protected final Log logger = LogFactory.getLog(getClass());

    /**
     * 标识将要注入的注解
     * eg: Autowired
     *     Value
     */
    private final Set<Class<? extends Annotation>> autowiredAnnotationTypes =
            new LinkedHashSet<Class<? extends Annotation>>();


    private final Map<String, InjectionMetadata> injectionMetadataCache =
            new ConcurrentHashMap<String, InjectionMetadata>(64);


    public AutowiredAnnotationBeanPostProcessor(){
        this.autowiredAnnotationTypes.add(Autowired.class);
        this.autowiredAnnotationTypes.add(InsertBean.class);
        this.autowiredAnnotationTypes.add(Value.class);
    }



    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        return null;
    }

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) {
        return null;
    }

    @Override
    public Object postProcessAfterInitialization(Object existingBean, String beanName) {
        return null;
    }

    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) {
        return false;
    }

    @Override
    public void postProcessPropertyValues(Object bean, String beanName) {

    }

    private InjectionMetadata findAutowiringMetadata(String beanName, Class<?> clazz) {
        // Quick check on the concurrent map first, with minimal locking.
        // Fall back to class name as cache key, for backwards compatibility with custom callers.
        String cacheKey = (null!=beanName) ? beanName : clazz.getName();
        InjectionMetadata metadata = this.injectionMetadataCache.get(cacheKey);
        if (InjectionMetadata.needsRefresh(metadata, clazz)) {
            synchronized (this.injectionMetadataCache) {
                metadata = this.injectionMetadataCache.get(cacheKey);
                if (InjectionMetadata.needsRefresh(metadata, clazz)) {
                    metadata = buildAutowiringMetadata(clazz);
                    this.injectionMetadataCache.put(cacheKey, metadata);
                }
            }
        }
        return metadata;
    }

    private InjectionMetadata buildAutowiringMetadata(Class<?> clazz) {
        LinkedList<InjectionMetadata.InjectedElement> elements = new LinkedList<InjectionMetadata.InjectedElement>();
        Class<?> targetClass = clazz;

        do {
            LinkedList<InjectionMetadata.InjectedElement> currElements = new LinkedList<InjectionMetadata.InjectedElement>();
            for (Field field : targetClass.getDeclaredFields()) {
                AnnotationTools.getAnnotationAttributeAsMap()
                AnnotationAttributes annotation = findAutowiredAnnotation(field);
                if (annotation != null) {
                    if (Modifier.isStatic(field.getModifiers())) {
                        if (logger.isWarnEnabled()) {
                            logger.warn("Autowired annotation is not supported on static fields: " + field);
                        }
                        continue;
                    }
                    boolean required = determineRequiredStatus(annotation);
                    currElements.add(new AutowiredFieldElement(field, required));
                }
            }
            for (Method method : targetClass.getDeclaredMethods()) {
                Method bridgedMethod = BridgeMethodResolver.findBridgedMethod(method);
                AnnotationAttributes annotation = BridgeMethodResolver.isVisibilityBridgeMethodPair(method, bridgedMethod) ?
                        findAutowiredAnnotation(bridgedMethod) : findAutowiredAnnotation(method);
                if (annotation != null && method.equals(ClassUtils.getMostSpecificMethod(method, clazz))) {
                    if (Modifier.isStatic(method.getModifiers())) {
                        if (logger.isWarnEnabled()) {
                            logger.warn("Autowired annotation is not supported on static methods: " + method);
                        }
                        continue;
                    }
                    if (method.getParameterTypes().length == 0) {
                        if (logger.isWarnEnabled()) {
                            logger.warn("Autowired annotation should be used on methods with actual parameters: " + method);
                        }
                    }
                    boolean required = determineRequiredStatus(annotation);
                    PropertyDescriptor pd = BeanUtils.findPropertyForMethod(method);
                    currElements.add(new AutowiredMethodElement(method, required, pd));
                }
            }
            elements.addAll(0, currElements);
            targetClass = targetClass.getSuperclass();
        }
        while (targetClass != null && targetClass != Object.class);

        return new InjectionMetadata(clazz, elements);
    }



    /**
     * Class representing injection information about an annotated field.
     */
    private class AutowiredFieldElement extends InjectionMetadata.InjectedElement {

        private final boolean required;

        private volatile boolean cached = false;

        private volatile Object cachedFieldValue;

        public AutowiredFieldElement(Field field, boolean required) {
            super(field, null);
            this.required = required;
        }

        @Override
        protected void inject(Object bean, String beanName, PropertyValues pvs) throws Throwable {
            Field field = (Field) this.member;
            try {
                Object value;
                if (this.cached) {
                    value = resolvedCachedArgument(beanName, this.cachedFieldValue);
                }
                else {
                    DependencyDescriptor desc = new DependencyDescriptor(field, this.required);
                    desc.setContainingClass(bean.getClass());
                    Set<String> autowiredBeanNames = new LinkedHashSet<String>(1);
                    TypeConverter typeConverter = beanFactory.getTypeConverter();
                    value = beanFactory.resolveDependency(desc, beanName, autowiredBeanNames, typeConverter);
                    synchronized (this) {
                        if (!this.cached) {
                            if (value != null || this.required) {
                                this.cachedFieldValue = desc;
                                registerDependentBeans(beanName, autowiredBeanNames);
                                if (autowiredBeanNames.size() == 1) {
                                    String autowiredBeanName = autowiredBeanNames.iterator().next();
                                    if (beanFactory.containsBean(autowiredBeanName)) {
                                        if (beanFactory.isTypeMatch(autowiredBeanName, field.getType())) {
                                            this.cachedFieldValue = new RuntimeBeanReference(autowiredBeanName);
                                        }
                                    }
                                }
                            }
                            else {
                                this.cachedFieldValue = null;
                            }
                            this.cached = true;
                        }
                    }
                }
                if (value != null) {
                    ReflectionUtils.makeAccessible(field);
                    field.set(bean, value);
                }
            }
            catch (Throwable ex) {
                throw new BeanCreationException("Could not autowire field: " + field, ex);
            }
        }
    }


    /**
     * Class representing injection information about an annotated method.
     */
    private class AutowiredMethodElement extends InjectionMetadata.InjectedElement {

        private final boolean required;

        private volatile boolean cached = false;

        private volatile Object[] cachedMethodArguments;

        public AutowiredMethodElement(Method method, boolean required, PropertyDescriptor pd) {
            super(method, pd);
            this.required = required;
        }

        @Override
        protected void inject(Object bean, String beanName, PropertyValues pvs) throws Throwable {
            if (checkPropertySkipping(pvs)) {
                return;
            }
            Method method = (Method) this.member;
            try {
                Object[] arguments;
                if (this.cached) {
                    // Shortcut for avoiding synchronization...
                    arguments = resolveCachedArguments(beanName);
                }
                else {
                    Class<?>[] paramTypes = method.getParameterTypes();
                    arguments = new Object[paramTypes.length];
                    DependencyDescriptor[] descriptors = new DependencyDescriptor[paramTypes.length];
                    Set<String> autowiredBeanNames = new LinkedHashSet<String>(paramTypes.length);
                    TypeConverter typeConverter = beanFactory.getTypeConverter();
                    for (int i = 0; i < arguments.length; i++) {
                        MethodParameter methodParam = new MethodParameter(method, i);
                        DependencyDescriptor desc = new DependencyDescriptor(methodParam, this.required);
                        desc.setContainingClass(bean.getClass());
                        descriptors[i] = desc;
                        Object arg = beanFactory.resolveDependency(desc, beanName, autowiredBeanNames, typeConverter);
                        if (arg == null && !this.required) {
                            arguments = null;
                            break;
                        }
                        arguments[i] = arg;
                    }
                    synchronized (this) {
                        if (!this.cached) {
                            if (arguments != null) {
                                this.cachedMethodArguments = new Object[arguments.length];
                                for (int i = 0; i < arguments.length; i++) {
                                    this.cachedMethodArguments[i] = descriptors[i];
                                }
                                registerDependentBeans(beanName, autowiredBeanNames);
                                if (autowiredBeanNames.size() == paramTypes.length) {
                                    Iterator<String> it = autowiredBeanNames.iterator();
                                    for (int i = 0; i < paramTypes.length; i++) {
                                        String autowiredBeanName = it.next();
                                        if (beanFactory.containsBean(autowiredBeanName)) {
                                            if (beanFactory.isTypeMatch(autowiredBeanName, paramTypes[i])) {
                                                this.cachedMethodArguments[i] = new RuntimeBeanReference(autowiredBeanName);
                                            }
                                        }
                                    }
                                }
                            }
                            else {
                                this.cachedMethodArguments = null;
                            }
                            this.cached = true;
                        }
                    }
                }
                if (arguments != null) {
                    ReflectionUtils.makeAccessible(method);
                    method.invoke(bean, arguments);
                }
            }
            catch (InvocationTargetException ex) {
                throw ex.getTargetException();
            }
            catch (Throwable ex) {
                throw new BeanCreationException("Could not autowire method: " + method, ex);
            }
        }

        private Object[] resolveCachedArguments(String beanName) {
            if (this.cachedMethodArguments == null) {
                return null;
            }
            Object[] arguments = new Object[this.cachedMethodArguments.length];
            for (int i = 0; i < arguments.length; i++) {
                arguments[i] = resolvedCachedArgument(beanName, this.cachedMethodArguments[i]);
            }
            return arguments;
        }
    }
}
