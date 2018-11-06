package org.smileframework.ioc.bean.context.beandefinition;

import org.smileframework.ioc.bean.context.beanfactory.AutowireCapableBeanFactory;
import org.smileframework.ioc.bean.context.factorybean.FactoryBean;
import org.smileframework.tool.clazz.LocalVariableTableParameterNameDiscoverer;
import org.smileframework.tool.clazz.ParameterNameDiscoverer;

import java.util.Map;

/**
 * @author liuxin
 * @version Id: AbstractBeanDefinition.java, v 0.1 2018/10/18 4:15 PM
 */
public class GenericBeanDefinition implements BeanDefinition {

    public static final String SCOPE_DEFAULT = "";


    int AUTOWIRE_NO = 0;


    public static final int AUTOWIRE_BY_NAME = AutowireCapableBeanFactory.AUTOWIRE_BY_NAME;


    public static final int AUTOWIRE_BY_TYPE = AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE;


    public static final int AUTOWIRE_CONSTRUCTOR = AutowireCapableBeanFactory.AUTOWIRE_CONSTRUCTOR;

    /**
     * 构造的依赖放到这里
     */
    private ConstructorArgumentValues constructorArgumentValues;

    /**
     * 从环境中获取到参数值
     */
    private Map<String,Object> propertyValues;

    private String beanName;

    private volatile Class beanClass;

    private String scope = SCOPE_DEFAULT;

    private boolean abstractFlag = false;

    private boolean lazyInit = false;

    private int autowireMode = AUTOWIRE_NO;

    private int dependencyCheck = -1;

    private String[] dependsOn;

    private boolean autowireCandidate = true;

    private boolean primary = false;

    private boolean factoryBean = false;

    /**
     * 是否执行实例化处理器
     */
    public boolean beforeInstantiationResolved = false;

    /**
     * 这两个初始化方法只存在xml中
     */
    private String initMethodName;

    /**
     * 这两个初始化方法只存在xml中
     */
    private String destroyMethodName;

    public boolean isBeforeInstantiationResolved() {
        return beforeInstantiationResolved;
    }

    public void setBeforeInstantiationResolved(boolean beforeInstantiationResolved) {
        this.beforeInstantiationResolved = beforeInstantiationResolved;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public boolean isFactoryBean() {
        return FactoryBean.class.isAssignableFrom(beanClass);
    }

    public ConstructorArgumentValues getConstructorArgumentValues() {
        return constructorArgumentValues;
    }

    public void setConstructorArgumentValues(ConstructorArgumentValues constructorArgumentValues) {
        this.constructorArgumentValues = constructorArgumentValues;
    }

    public Map<String, Object> getPropertyValues() {
        return propertyValues;
    }

    public void setPropertyValues(Map<String, Object> propertyValues) {
        this.propertyValues = propertyValues;
    }

    public Class getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class beanClass) {
        this.beanClass = beanClass;
    }

    public boolean isAbstractFlag() {
        return abstractFlag;
    }

    public void setAbstractFlag(boolean abstractFlag) {
        this.abstractFlag = abstractFlag;
    }

    public int getAutowireMode() {
        return autowireMode;
    }

    public void setAutowireMode(int autowireMode) {
        this.autowireMode = autowireMode;
    }

    public int getDependencyCheck() {
        return dependencyCheck;
    }

    public void setDependencyCheck(int dependencyCheck) {
        this.dependencyCheck = dependencyCheck;
    }

    public void setAutowireCandidate(boolean autowireCandidate) {
        this.autowireCandidate = autowireCandidate;
    }

    public String getInitMethodName() {
        return initMethodName;
    }

    public void setInitMethodName(String initMethodName) {
        this.initMethodName = initMethodName;
    }

    public String getDestroyMethodName() {
        return destroyMethodName;
    }

    public void setDestroyMethodName(String destroyMethodName) {
        this.destroyMethodName = destroyMethodName;
    }

    @Override
    public String getBeanClassName() {
        return null;
    }

    @Override
    public String getScope() {
        return this.scope;
    }

    @Override
    public void setScope(String scope) {
        this.scope = scope;
    }

    @Override
    public boolean isLazyInit() {
        return this.lazyInit;
    }

    @Override
    public void setLazyInit(boolean lazyInit) {
        this.lazyInit = lazyInit;
    }

    @Override
    public String[] getDependsOn() {
        return this.dependsOn;
    }

    @Override
    public void setDependsOn(String[] dependsOn) {
        this.dependsOn = dependsOn;
    }

    @Override
    public boolean isSingleton() {
        return SCOPE_SINGLETON.equals(scope) || SCOPE_DEFAULT.equals(scope);
    }

    @Override
    public boolean isPrototype() {
        return SCOPE_PROTOTYPE.equals(scope);
    }

    @Override
    public boolean isAbstract() {
        return this.isAbstract();
    }

    @Override
    public boolean isPrimary() {
        return this.primary;
    }

    @Override
    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    @Override
    public boolean isAutowireCandidate() {
        return this.isAutowireCandidate();
    }

}
