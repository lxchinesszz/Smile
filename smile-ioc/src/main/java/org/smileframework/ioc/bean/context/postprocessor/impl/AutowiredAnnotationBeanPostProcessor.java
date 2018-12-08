package org.smileframework.ioc.bean.context.postprocessor.impl;

import com.google.common.collect.Lists;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.smileframework.ioc.bean.annotation.Autowired;
import org.smileframework.ioc.bean.annotation.InsertBean;
import org.smileframework.ioc.bean.annotation.Value;
import org.smileframework.ioc.bean.context.aware.BeanFactoryAware;
import org.smileframework.ioc.bean.context.beanfactory.BeanFactory;
import org.smileframework.ioc.bean.context.beanfactory.exception.BeanCreationException;
import org.smileframework.ioc.bean.context.beanfactory.impl.DefaultListableBeanFactory;
import org.smileframework.ioc.bean.context.beanfactory.convert.TypeConverter;
import org.smileframework.ioc.bean.context.postprocessor.InstantiationAwareBeanPostProcessor;
import org.smileframework.ioc.util.ReflectionUtils;
import org.smileframework.tool.annotation.AnnotationTools;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 实现Autowired注入和Value属性值注入
 * @author liuxin
 * @version Id: AutowiredAnnotationBeanPostProcessor.java, v 0.1 2018/10/30 2:11 PM
 */
public class AutowiredAnnotationBeanPostProcessor implements InstantiationAwareBeanPostProcessor, BeanFactoryAware {

    protected final Log logger = LogFactory.getLog(getClass());


    private DefaultListableBeanFactory beanFactory;
    /**
     * 标识将要注入的注解
     * eg: Autowired
     * Value
     */
    private final Set<Class<? extends Annotation>> autowiredAnnotationTypes =
            new LinkedHashSet<Class<? extends Annotation>>();


    private final Map<String, InjectionMetadata> injectionMetadataCache =
            new ConcurrentHashMap<String, InjectionMetadata>(64);


    public AutowiredAnnotationBeanPostProcessor() {
        this.autowiredAnnotationTypes.add(Autowired.class);
        this.autowiredAnnotationTypes.add(Value.class);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        return bean;
    }


    @Override
    public Object postProcessAfterInitialization(Object existingBean, String beanName) {
        return existingBean;
    }

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) {
        return null;
    }

    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) {
        return false;
    }

    @Override
    public void postProcessPropertyValues(Object bean, String beanName) {
        InjectionMetadata autowiringMetadata = findAutowiringMetadata(beanName, bean.getClass());
        if (autowiringMetadata.isDependency()) {
            return;
        }
        try {
            autowiringMetadata.inject(bean, beanName, null);
        } catch (BeanCreationException var7) {
            throw var7;
        } catch (Throwable var8) {
            throw new BeanCreationException(beanName, "Injection of autowired dependencies failed", var8);
        }
    }

    private InjectionMetadata findAutowiringMetadata(String beanName, Class<?> clazz) {
        // Quick check on the concurrent map first, with minimal locking.
        // Fall back to class name as cache key, for backwards compatibility with custom callers.
        String cacheKey = (null != beanName) ? beanName : clazz.getName();
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


    /**
     * 判断是否必须注入主要判断
     * required字段
     *
     * @param memberObj
     * @return
     */
    protected boolean determineRequiredStatus(Object memberObj) {
        if (memberObj instanceof Field) {
            boolean containsAutowiredAnnotation = AnnotationTools.isContainsAnnotation(memberObj, Autowired.class);
            if (containsAutowiredAnnotation) {
                return ((Field) memberObj).getDeclaredAnnotation(Autowired.class).required();
            }
            boolean containsInsertBeanAnnotation = AnnotationTools.isContainsAnnotation(memberObj, InsertBean.class);
            if (containsInsertBeanAnnotation) {
                return ((Field) memberObj).getDeclaredAnnotation(InsertBean.class).required();
            }
        }
        if (memberObj instanceof Method) {
            boolean containsAutowiredAnnotation = AnnotationTools.isContainsAnnotation(memberObj, Autowired.class);
            if (containsAutowiredAnnotation) {
                return ((Method) memberObj).getDeclaredAnnotation(Autowired.class).required();
            }
            boolean containsInsertBeanAnnotation = AnnotationTools.isContainsAnnotation(memberObj, InsertBean.class);
            if (containsInsertBeanAnnotation) {
                return ((Method) memberObj).getDeclaredAnnotation(InsertBean.class).required();
            }
        }
        //默认必须注入
        return true;
    }


    private InjectionMetadata buildAutowiringMetadata(Class<?> clazz) {
        LinkedList<InjectionMetadata.InjectedElement> elements = new LinkedList<InjectionMetadata.InjectedElement>();
        Class<?> targetClass = clazz;

        do {
            LinkedList<InjectionMetadata.InjectedElement> currElements = new LinkedList<InjectionMetadata.InjectedElement>();
            for (Field field : targetClass.getDeclaredFields()) {
                //首先判断Field上是否有注入标志
                boolean containsAnnotation = AnnotationTools.isContainsAnnotation(field, Lists.newArrayList(autowiredAnnotationTypes));
                if (containsAnnotation) {
                    //带有static的不支持注入
                    if (Modifier.isStatic(field.getModifiers())) {
                        if (logger.isWarnEnabled()) {
                            logger.warn("Autowired annotation is not supported on static fields: " + field);
                        }
                        continue;
                    }
                    boolean required = determineRequiredStatus(field);
                    currElements.add(new AutowiredFieldElement(field, required));
                }
            }
            for (Method method : targetClass.getDeclaredMethods()) {
                boolean containsAnnotation = AnnotationTools.isContainsAnnotation(method,Lists.newArrayList(autowiredAnnotationTypes));
                if (containsAnnotation) {
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
                    boolean required = determineRequiredStatus(method);
                    //获取属性描述
//                    PropertyDescriptor pd = BeanUtils.findPropertyForMethod(method);
                    PropertyDescriptor pd = null;
                    currElements.add(new AutowiredMethodElement(method, required, pd));
                }
            }
            elements.addAll(0, currElements);
            targetClass = targetClass.getSuperclass();
        }
        while (targetClass != null && targetClass != Object.class);

        return new InjectionMetadata(clazz, elements);
    }


    private Object resolvedCachedArgument(String beanName, Object cachedArgument) {
        if (cachedArgument instanceof DependencyDescriptor) {
            DependencyDescriptor descriptor = (DependencyDescriptor) cachedArgument;
            return this.beanFactory.resolveDependency(descriptor, beanName, null, null);
        } else {
            return cachedArgument;
        }
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
                Object value = null;
                if (this.cached) {
                    value = resolvedCachedArgument(beanName, this.cachedFieldValue);
                } else {
                    //将一个beanName依赖的bean给记录下来，给BeanFactory维护，当前先不实现。
                    Set<String> autowiredBeanNames = new LinkedHashSet<String>(1);
                    TypeConverter typeConverter = beanFactory.getTypeConverter();
                    DependencyDescriptor desc = new DependencyDescriptor(field, this.required);
                    desc.setContainingClass(bean.getClass());
                    Annotation[] annotations = desc.getAnnotations();
                    Value valueAnnotation = AnnotationTools.findAnnotation(Arrays.asList(annotations), Value.class);
                    if (null != valueAnnotation) {
                        value = beanFactory.getPropertyResolver().resolvePlaceholders(valueAnnotation.value());
                        value = typeConverter.convertIfNecessary(value,field.getType());
                    }else if (Modifier.isInterface(field.getType().getModifiers())||Modifier.isAbstract(field.getType().getModifiers())){
                        String[] beanNamesForType = beanFactory.getBeanNamesForType(field.getType());
                        if (beanNamesForType.length ==1){
                            value = beanFactory.getBean(beanNamesForType[0]);
                        }
                    } else {
                        value = beanFactory.resolveDependency(desc, beanName, autowiredBeanNames, typeConverter);
                    }
                    //当发现value是空，但是
                    if (value == null && !this.required){
                        value = null;
                    }
                }
                if (value != null) {
                    ReflectionUtils.makeAccessible(field);
                    field.set(bean, value);
                }
            } catch (Throwable ex) {
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
            Method method = (Method) this.member;
            try {
                Object[] arguments;
                if (this.cached) {
                    // Shortcut for avoiding synchronization...
                    arguments = resolveCachedArguments(beanName);
                } else {
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
                        Annotation[] annotations = desc.getAnnotations();
                        Value valueAnnotation = AnnotationTools.findAnnotation(Arrays.asList(annotations), Value.class);
                        Object arg = null;
                        if (null != valueAnnotation) {
                            arg = beanFactory.getPropertyResolver().resolvePlaceholders(valueAnnotation.value());
                            arg = typeConverter.convertIfNecessary(arg,paramTypes[i]);
                        }else if (Modifier.isInterface(paramTypes[i].getModifiers())||Modifier.isAbstract(paramTypes[i].getModifiers())){
                            String[] beanNamesForType = beanFactory.getBeanNamesForType(paramTypes[i]);
                            if (beanNamesForType.length ==1){
                                arg = beanFactory.getBean(beanNamesForType[0]);
                            }
                        }  else {
                            arg = beanFactory.resolveDependency(desc, beanName, autowiredBeanNames, typeConverter);
                        }
                        if (arg == null && !this.required) {
                            arguments = null;
                            break;
                        }
                        arguments[i] = arg;
                    }
                }
                if (arguments != null) {
                    ReflectionUtils.makeAccessible(method);
                    method.invoke(bean, arguments);
                }
            } catch (InvocationTargetException ex) {
                throw ex.getTargetException();
            } catch (Throwable ex) {
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
