package org.smileframework.ioc.bean.context.postprocessor.impl;

import org.smileframework.tool.asserts.Assert;
import org.smileframework.tool.clazz.ParameterNameDiscoverer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author liuxin
 * @version Id: DependencyDescriptor.java, v 0.1 2018/11/19 3:06 PM
 */
public class DependencyDescriptor {

    private transient MethodParameter methodParameter;

    private transient Field field;

    private Class<?> declaringClass;

    private Class<?> containingClass;

    private String methodName;

    private Class<?>[] parameterTypes;

    private int parameterIndex;

    private String fieldName;

    private final boolean required;

    private final boolean eager;

    private int nestingLevel = 1;

    private transient Annotation[] fieldAnnotations;


    /**
     * Create a new descriptor for a method or constructor parameter.
     * Considers the dependency as 'eager'.
     * @param methodParameter the MethodParameter to wrap
     * @param required whether the dependency is required
     */
    public DependencyDescriptor(MethodParameter methodParameter, boolean required) {
        this(methodParameter, required, true);
    }

    /**
     * Create a new descriptor for a method or constructor parameter.
     * @param methodParameter the MethodParameter to wrap
     * @param required whether the dependency is required
     * @param eager whether this dependency is 'eager' in the sense of
     * eagerly resolving potential target beans for type matching
     */
    public DependencyDescriptor(MethodParameter methodParameter, boolean required, boolean eager) {
        Assert.notNull(methodParameter, "MethodParameter must not be null");
        this.methodParameter = methodParameter;
        this.declaringClass = methodParameter.getDeclaringClass();
        this.containingClass = methodParameter.getContainingClass();
        if (this.methodParameter.getMethod() != null) {
            this.methodName = methodParameter.getMethod().getName();
            this.parameterTypes = methodParameter.getMethod().getParameterTypes();
        }
        else {
            this.parameterTypes = methodParameter.getConstructor().getParameterTypes();
        }
        this.parameterIndex = methodParameter.getParameterIndex();
        this.required = required;
        this.eager = eager;
    }

    /**
     * Create a new descriptor for a field.
     * Considers the dependency as 'eager'.
     * @param field the field to wrap
     * @param required whether the dependency is required
     */
    public DependencyDescriptor(Field field, boolean required) {
        this(field, required, true);
    }

    /**
     * Create a new descriptor for a field.
     * @param field the field to wrap
     * @param required whether the dependency is required
     * @param eager whether this dependency is 'eager' in the sense of
     * eagerly resolving potential target beans for type matching
     */
    public DependencyDescriptor(Field field, boolean required, boolean eager) {
        Assert.notNull(field, "Field must not be null");
        this.field = field;
        this.declaringClass = field.getDeclaringClass();
        this.fieldName = field.getName();
        this.required = required;
        this.eager = eager;
    }

    /**
     * Copy constructor.
     * @param original the original descriptor to create a copy from
     */
    public DependencyDescriptor(DependencyDescriptor original) {
        this.methodParameter = (original.methodParameter != null ? new MethodParameter(original.methodParameter) : null);
        this.field = original.field;
        this.declaringClass = original.declaringClass;
        this.containingClass = original.containingClass;
        this.methodName = original.methodName;
        this.parameterTypes = original.parameterTypes;
        this.parameterIndex = original.parameterIndex;
        this.fieldName = original.fieldName;
        this.required = original.required;
        this.eager = original.eager;
        this.nestingLevel = original.nestingLevel;
        this.fieldAnnotations = original.fieldAnnotations;
    }


    /**
     * Return the wrapped MethodParameter, if any.
     * <p>Note: Either MethodParameter or Field is available.
     * @return the MethodParameter, or {@code null} if none
     */
    public MethodParameter getMethodParameter() {
        return this.methodParameter;
    }

    /**
     * Return the wrapped Field, if any.
     * <p>Note: Either MethodParameter or Field is available.
     * @return the Field, or {@code null} if none
     */
    public Field getField() {
        return this.field;
    }

    /**
     * Return whether this dependency is required.
     */
    public boolean isRequired() {
        return this.required;
    }

    /**
     * Return whether this dependency is 'eager' in the sense of
     * eagerly resolving potential target beans for type matching.
     */
    public boolean isEager() {
        return this.eager;
    }

    public void setContainingClass(Class<?> containingClass) {
        this.containingClass = containingClass;
    }

    /**
     * Increase this descriptor's nesting level.
     * @see MethodParameter#increaseNestingLevel()
     */
    public void increaseNestingLevel() {
        this.nestingLevel++;
        if (this.methodParameter != null) {
            this.methodParameter.increaseNestingLevel();
        }
    }



    /**
     * Return whether a fallback match is allowed.
     * that a fallback match is acceptable as well.
     */
    public boolean fallbackMatchAllowed() {
        return false;
    }

    /**
     * Return a variant of this descriptor that is intended for a fallback match.
     * @see #fallbackMatchAllowed()
     */
    public DependencyDescriptor forFallbackMatch() {
        return new DependencyDescriptor(this) {
            @Override
            public boolean fallbackMatchAllowed() {
                return true;
            }
        };
    }

    /**
     * Initialize parameter name discovery for the underlying method parameter, if any.
     * <p>This method does not actually try to retrieve the parameter name at
     * this point; it just allows discovery to happen when the application calls
     * {@link #getDependencyName()} (if ever).
     */
    public void initParameterNameDiscovery(ParameterNameDiscoverer parameterNameDiscoverer) {
        if (this.methodParameter != null) {
            this.methodParameter.initParameterNameDiscovery(parameterNameDiscoverer);
        }
    }

    /**
     * Determine the name of the wrapped parameter/field.
     * @return the declared name (never {@code null})
     */
    public String getDependencyName() {
        return (this.field != null ? this.field.getName() : this.methodParameter.getParameterName());
    }

    /**
     * Determine the declared (non-generic) type of the wrapped parameter/field.
     * @return the declared type (never {@code null})
     */
    public Class<?> getDependencyType() {
        if (this.field != null) {
            if (this.nestingLevel > 1) {
                Type type = this.field.getGenericType();
                if (type instanceof ParameterizedType) {
                    Type[] args = ((ParameterizedType) type).getActualTypeArguments();
                    Type arg = args[args.length - 1];
                    if (arg instanceof Class) {
                        return (Class<?>) arg;
                    }
                    else if (arg instanceof ParameterizedType) {
                        arg = ((ParameterizedType) arg).getRawType();
                        if (arg instanceof Class) {
                            return (Class<?>) arg;
                        }
                    }
                }
                return Object.class;
            }
            else {
                return this.field.getType();
            }
        }
        else {
            return this.methodParameter.getNestedParameterType();
        }
    }


    /**
     * Obtain the annotations associated with the wrapped parameter/field, if any.
     */
    public Annotation[] getAnnotations() {
        if (this.field != null) {
            if (this.fieldAnnotations == null) {
                this.fieldAnnotations = this.field.getAnnotations();
            }
            return this.fieldAnnotations;
        }
        else {
            return this.methodParameter.getParameterAnnotations();
        }
    }


    //---------------------------------------------------------------------
    // Serialization support
    //---------------------------------------------------------------------

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        // Rely on default serialization; just initialize state after deserialization.
        ois.defaultReadObject();

        // Restore reflective handles (which are unfortunately not serializable)
        try {
            if (this.fieldName != null) {
                this.field = this.declaringClass.getDeclaredField(this.fieldName);
            }
            else {
                if (this.methodName != null) {
                    this.methodParameter = new MethodParameter(
                            this.declaringClass.getDeclaredMethod(this.methodName, this.parameterTypes), this.parameterIndex);
                }
                else {
                    this.methodParameter = new MethodParameter(
                            this.declaringClass.getDeclaredConstructor(this.parameterTypes), this.parameterIndex);
                }
                for (int i = 1; i < this.nestingLevel; i++) {
                    this.methodParameter.increaseNestingLevel();
                }
            }
        }
        catch (Throwable ex) {
            throw new IllegalStateException("Could not find original class structure", ex);
        }
    }
}
