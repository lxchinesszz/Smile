package org.smileframework.ioc.bean.core.env;

/**
 * 配置文件引用接口
 *
 * 主要实现@value功能接口
 * @author liuxin
 * @version Id: ConfigurablePropertyResolver.java, v 0.1 2018/10/16 7:53 PM
 */
public interface ConfigurablePropertyResolver extends PropertyResolver {

    void setPlaceholderPrefix(String placeholderPrefix);


    void setPlaceholderSuffix(String placeholderSuffix);


    void setValueSeparator(String valueSeparator);

    /**
     * Specify which properties must be present, to be verified by
     * {@link #validateRequiredProperties()}.
     */
    void setRequiredProperties(String... requiredProperties);


    void validateRequiredProperties();

    void setIgnoreUnresolvableNestedPlaceholders(boolean ignoreUnresolvableNestedPlaceholders);
}
