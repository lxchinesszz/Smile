package org.smileframework.ioc.bean.context.beandefinition;

import org.smileframework.ioc.bean.context.beanfactory.AutowireCapableBeanFactory;
import org.smileframework.ioc.bean.context.factorybean.FactoryBean;
import org.smileframework.tool.clazz.ClassTools;
import org.smileframework.tool.clazz.LocalVariableTableParameterNameDiscoverer;
import org.smileframework.tool.clazz.ParameterNameDiscoverer;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;
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

    private boolean emptyConstructorFlag;
    /**
     * 构造的依赖放到这里
     */
    private List<ConstructorInfo> constructorInfo;

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

    /**
     * 是否有空构造
     * @return
     */
    public boolean isEmptyConstructorFlag() {
        return emptyConstructorFlag;
    }

    public void setEmptyConstructorFlag(boolean emptyConstructorFlag) {
        this.emptyConstructorFlag = emptyConstructorFlag;
    }

    public boolean isBeforeInstantiationResolved() {
        return beforeInstantiationResolved;
    }

    public void setBeforeInstantiationResolved(boolean beforeInstantiationResolved) {
        this.beforeInstantiationResolved = beforeInstantiationResolved;
    }

    @Override
    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    @Override
    public boolean isFactoryBean() {
        return FactoryBean.class.isAssignableFrom(beanClass);
    }

    @Override
    public List<ConstructorInfo> getConstructorInfo() {
        return constructorInfo;
    }

    public void setConstructorInfo(List<ConstructorInfo> constructorInfo) {
        this.constructorInfo = constructorInfo;
    }

    public Map<String, Object> getPropertyValues() {
        return propertyValues;
    }

    public void setPropertyValues(Map<String, Object> propertyValues) {
        this.propertyValues = propertyValues;
    }

    @Override
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
        return ClassTools.getShortName(this.beanClass);
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

    @Override
    public String toString() {
        return "GenericBeanDefinition{" +
                "AUTOWIRE_NO=" + AUTOWIRE_NO +
                ", constructorInfo=" + constructorInfo +
                ", propertyValues=" + propertyValues +
                ", beanName='" + beanName + '\'' +
                ", beanClass=" + beanClass +
                ", scope='" + scope + '\'' +
                ", abstractFlag=" + abstractFlag +
                ", lazyInit=" + lazyInit +
                ", autowireMode=" + autowireMode +
                ", dependencyCheck=" + dependencyCheck +
                ", dependsOn=" + Arrays.toString(dependsOn) +
                ", autowireCandidate=" + autowireCandidate +
                ", primary=" + primary +
                ", factoryBean=" + factoryBean +
                ", beforeInstantiationResolved=" + beforeInstantiationResolved +
                ", initMethodName='" + initMethodName + '\'' +
                ", destroyMethodName='" + destroyMethodName + '\'' +
                '}';
    }
}
