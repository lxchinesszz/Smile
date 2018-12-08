package org.smileframework.ioc.bean.core.env;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.smileframework.tool.asserts.Assert;

import java.util.*;

import static java.lang.String.format;

/**
 * @author liuxin
 * @version Id: AbstractEnvironment.java, v 0.1 2018/10/16 7:29 PM
 */
public abstract class AbstractEnvironment implements ConfigurableEnvironment {

    protected final Log logger = LogFactory.getLog(getClass());

    protected static final String RESERVED_DEFAULT_PROFILE_NAME = "default";

    /**
     * @see ConfigurableEnvironment#setActiveProfiles
     */
    public static final String ACTIVE_PROFILES_PROPERTY_NAME = "spring.profiles.active";

    /**
     * Name of property to set to specify profiles active by default: {@value}. Value may
     *
     * @see ConfigurableEnvironment#setDefaultProfiles
     */
    public static final String DEFAULT_PROFILES_PROPERTY_NAME = "spring.profiles.default";

    /**
     * 激活的配置文件信息
     */
    private Set<String> activeProfiles = new LinkedHashSet<String>();

    /**
     * 默认的配置文件信息
     */
    private Set<String> defaultProfiles = new LinkedHashSet<String>(Collections.singleton(RESERVED_DEFAULT_PROFILE_NAME));

    /**
     * 环境信息
     */
    private final MutablePropertySources propertySources = new MutablePropertySources(this.logger);

    /**
     * 参数解析器
     */
    private final ConfigurablePropertyResolver propertyResolver =
            new PropertySourcesPropertyResolver(this.propertySources);


    /**
     * 所有的配置信息由customizePropertySources注入
     */
    public AbstractEnvironment() {
        customizePropertySources(this.propertySources);
        if (this.logger.isDebugEnabled()) {
            this.logger.debug(format(
                    "Initialized %s with PropertySources %s", getClass().getSimpleName(), this.propertySources));
        }
    }

    /**
     * 留给子类来实现
     *
     * @param propertySources
     */
    protected void customizePropertySources(MutablePropertySources propertySources) {
    }


    public MutablePropertySources getPropertySources() {
        return propertySources;
    }

    /**
     * Java运行环境信息
     *
     * @return
     */
    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> getSystemProperties() {
        return (Map) System.getProperties();

    }

    /**
     * 系统PATH信息
     *
     * @return
     */
    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> getSystemEnvironment() {
        return (Map) System.getenv();
    }


    /**
     * 是否包含某个环境信息
     * JVM环境信息
     * 系统PATH环境信息
     *
     * @see StandardEnvironment
     * public static final String SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME = "systemEnvironment";
     * String SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME = "systemProperties";
     */
    @Override
    public boolean containsProperty(String key) {
        return propertyResolver.containsProperty(key);
    }

    @Override
    public String getProperty(String key) {
        return propertyResolver.getProperty(key);
    }

    @Override
    public String getProperty(String key, String defaultValue) {
        return propertyResolver.getProperty(key, defaultValue);
    }

    @Override
    public <T> T getProperty(String key, Class<T> targetType) {
        return propertyResolver.getProperty(key, targetType);
    }

    @Override
    public void setActiveProfiles(String... profiles) {
        Assert.notNull(profiles, "Profile array must not be null");
        this.activeProfiles.clear();
        for (String profile : profiles) {
            this.activeProfiles.add(profile);
        }
    }

    @Override
    public void addActiveProfile(String profile) {
        doGetActiveProfiles();
        this.activeProfiles.add(profile);
    }

    @Override
    public String[] getActiveProfiles() {
        if (this.activeProfiles.isEmpty()) {
            String profiles = getProperty(ACTIVE_PROFILES_PROPERTY_NAME);
            this.activeProfiles.add(profiles);
        }
        return (String[]) activeProfiles.toArray();
    }

    @Override
    public <T> T getProperty(String key, Class<T> targetType, T defaultValue) {
        return propertyResolver.getProperty(key,targetType,defaultValue);
    }

    protected Set<String> doGetActiveProfiles() {
        if (this.activeProfiles.isEmpty()) {
            String profiles = getProperty(ACTIVE_PROFILES_PROPERTY_NAME);
            this.activeProfiles.add(profiles);
        }
        return this.activeProfiles;
    }
    protected Set<String> doGetDefaultProfiles() {
        if (this.defaultProfiles.equals(Collections.singleton(RESERVED_DEFAULT_PROFILE_NAME))) {
            String profiles = getProperty(DEFAULT_PROFILES_PROPERTY_NAME);
            setDefaultProfiles(profiles);
        }
        return this.defaultProfiles;
    }




    @Override
    public void setDefaultProfiles(String... profiles) {
        Assert.notNull(profiles, "Profile array must not be null");
        this.defaultProfiles.clear();
        for (String profile : profiles) {
            this.defaultProfiles.add(profile);
        }
    }

    @Override
    public <T> Class<T> getPropertyAsClass(String key, Class<T> targetType) {
        return propertyResolver.getPropertyAsClass(key,targetType);
    }

    @Override
    public String[] getDefaultProfiles() {
        return (String[]) defaultProfiles.toArray();
    }

    @Override
    public String getRequiredProperty(String key) throws IllegalStateException {
        return propertyResolver.getRequiredProperty(key);
    }

    @Override
    public boolean acceptsProfiles(String... profiles) {
        return false;
    }

    @Override
    public <T> T getRequiredProperty(String key, Class<T> targetType) throws IllegalStateException {
        return propertyResolver.getRequiredProperty(key,targetType);
    }

    @Override
    public void merge(ConfigurableEnvironment parent) {

    }

    @Override
    public String resolvePlaceholders(String text) {
        return propertyResolver.resolvePlaceholders(text);
    }

    @Override
    public String resolveRequiredPlaceholders(String text) throws IllegalArgumentException {
        return propertyResolver.resolveRequiredPlaceholders(text);
    }
}
