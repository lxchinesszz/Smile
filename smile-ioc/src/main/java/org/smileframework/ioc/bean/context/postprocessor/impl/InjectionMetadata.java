package org.smileframework.ioc.bean.context.postprocessor.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.smileframework.ioc.bean.context.beandefinition.GenericBeanDefinition;
import org.smileframework.ioc.util.ReflectionUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author liuxin
 * @version Id: InjectionMetadata.java, v 0.1 2018/10/30 2:40 PM
 */
public class InjectionMetadata {
    private final Log logger = LogFactory.getLog(InjectionMetadata.class);

    private final Class<?> targetClass;

    private final Collection<InjectedElement> injectedElements;

    private volatile Set<InjectedElement> checkedElements;


    public InjectionMetadata(Class<?> targetClass, Collection<InjectedElement> elements) {
        this.targetClass = targetClass;
        this.injectedElements = elements;
    }

    public void checkConfigMembers(GenericBeanDefinition beanDefinition) {
        Set<InjectedElement> checkedElements = new LinkedHashSet<InjectedElement>(this.injectedElements.size());
        for (InjectedElement element : this.injectedElements) {
            checkedElements.add(element);
            if (logger.isDebugEnabled()) {
                logger.debug("Registered injected element on class [" + this.targetClass.getName() + "]: " + element);
            }

        }
        this.checkedElements = checkedElements;
    }

    public void inject(Object target, String beanName, PropertyValues pvs) throws Throwable {
        Collection<InjectedElement> elementsToIterate =
                (this.checkedElements != null ? this.checkedElements : this.injectedElements);
        if (!elementsToIterate.isEmpty()) {
            boolean debug = logger.isDebugEnabled();
            for (InjectedElement element : elementsToIterate) {
                if (debug) {
                    logger.debug("Processing injected method of bean '" + beanName + "': " + element);
                }
                element.inject(target, beanName, pvs);
            }
        }
    }


    public static boolean needsRefresh(InjectionMetadata metadata, Class<?> clazz) {
        return (metadata == null || !metadata.targetClass.equals(clazz));
    }


    public static abstract class InjectedElement {

        protected final Member member;

        protected final boolean isField;

        protected final PropertyDescriptor pd;

        protected volatile Boolean skip;

        protected InjectedElement(Member member, PropertyDescriptor pd) {
            this.member = member;
            this.isField = (member instanceof Field);
            this.pd = pd;
        }

        public final Member getMember() {
            return this.member;
        }

        protected final Class<?> getResourceType() {
            if (this.isField) {
                return ((Field) this.member).getType();
            } else if (this.pd != null) {
                return this.pd.getPropertyType();
            } else {
                return ((Method) this.member).getParameterTypes()[0];
            }
        }

        protected final void checkResourceType(Class<?> resourceType) {
            if (this.isField) {
                Class<?> fieldType = ((Field) this.member).getType();
                if (!(resourceType.isAssignableFrom(fieldType) || fieldType.isAssignableFrom(resourceType))) {
                    throw new IllegalStateException("Specified field type [" + fieldType +
                            "] is incompatible with resource type [" + resourceType.getName() + "]");
                }
            } else {
                Class<?> paramType =
                        (this.pd != null ? this.pd.getPropertyType() : ((Method) this.member).getParameterTypes()[0]);
                if (!(resourceType.isAssignableFrom(paramType) || paramType.isAssignableFrom(resourceType))) {
                    throw new IllegalStateException("Specified parameter type [" + paramType +
                            "] is incompatible with resource type [" + resourceType.getName() + "]");
                }
            }
        }

        /**
         * Either this or {@link #getResourceToInject} needs to be overridden.
         */
        protected void inject(Object target, String requestingBeanName, PropertyValues pvs) throws Throwable {
            if (this.isField) {
                Field field = (Field) this.member;
                ReflectionUtils.makeAccessible(field);
                field.set(target, getResourceToInject(target, requestingBeanName));
            } else {
                if (checkPropertySkipping(pvs)) {
                    return;
                }
                try {
                    Method method = (Method) this.member;
                    ReflectionUtils.makeAccessible(method);
                    method.invoke(target, getResourceToInject(target, requestingBeanName));
                } catch (InvocationTargetException ex) {
                    throw ex.getTargetException();
                }
            }
        }

        /**
         * Checks whether this injector's property needs to be skipped due to
         * an explicit property value having been specified. Also marks the
         * affected property as processed for other processors to ignore it.
         */
        protected boolean checkPropertySkipping(PropertyValues pvs) {
            if (this.skip != null) {
                return this.skip;
            }
            if (pvs == null) {
                this.skip = false;
                return false;
            }
            synchronized (pvs) {
                if (this.skip != null) {
                    return this.skip;
                }
                if (this.pd != null) {
                    if (pvs.contains(this.pd.getName())) {
                        // Explicit value provided as part of the bean definition.
                        this.skip = true;
                        return true;
                    } else if (pvs instanceof MutablePropertyValues) {
                        ((MutablePropertyValues) pvs).registerProcessedProperty(this.pd.getName());
                    }
                }
                this.skip = false;
                return false;
            }
        }

        /**
         * Either this or {@link #inject} needs to be overridden.
         */
        protected Object getResourceToInject(Object target, String requestingBeanName) {
            return null;
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof InjectedElement)) {
                return false;
            }
            InjectedElement otherElement = (InjectedElement) other;
            return this.member.equals(otherElement.member);
        }

        @Override
        public int hashCode() {
            return this.member.getClass().hashCode() * 29 + this.member.getName().hashCode();
        }

        @Override
        public String toString() {
            return getClass().getSimpleName() + " for " + this.member;
        }
    }

}
