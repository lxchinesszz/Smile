package org.smileframework.ioc.bean.core.env;

/**
 * 主要是实现@Value
 *
 * @author liuxin
 * @version Id: PropertySourcesPropertyResolver.java, v 0.1 2018/10/16 7:55 PM
 */
public class PropertySourcesPropertyResolver extends AbstractPropertyResolver {

    private final PropertySources propertySources;

    public PropertySourcesPropertyResolver(PropertySources propertySources) {
        this.propertySources = propertySources;
    }

    @Override
    public boolean containsProperty(String key) {
        if (this.propertySources != null) {
            for (PropertySource<?> propertySource : this.propertySources) {
                if (propertySource.containsProperty(key)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String getProperty(String key) {
        return getProperty(key, String.class, true);
    }


    @Override
    public <T> T getProperty(String key, Class<T> targetType) {
        return getProperty(key, targetType, true);
    }

    @Override
    public void setValueSeparator(String valueSeparator) {

    }

    @Override
    public <T> T getProperty(String key, Class<T> targetType, T defaultValue) {
        return null;
    }

    @Override
    public <T> Class<T> getPropertyAsClass(String key, Class<T> targetType) {
        return null;
    }

    @Override
    public void setRequiredProperties(String... requiredProperties) {

    }

    @Override
    public String getRequiredProperty(String key) throws IllegalStateException {
        return null;
    }

    @Override
    public void validateRequiredProperties() {

    }

    @Override
    public void setIgnoreUnresolvableNestedPlaceholders(boolean ignoreUnresolvableNestedPlaceholders) {

    }

    @Override
    protected String getPropertyAsRawString(String key) {
        return getProperty(key,String.class,false);
    }

    protected <T> T getProperty(String key, Class<T> targetValueType, boolean resolveNestedPlaceholders) {
        boolean debugEnabled = logger.isDebugEnabled();
        if (logger.isTraceEnabled()) {
            logger.trace(String.format("getProperty(\"%s\", %s)", key, targetValueType.getSimpleName()));
        }
        if (this.propertySources != null) {
            for (PropertySource<?> propertySource : this.propertySources) {
                if (debugEnabled) {
                    logger.debug(String.format("Searching for key '%s' in [%s]", key, propertySource.getName()));
                }
                Object value;
                if ((value = propertySource.getProperty(key)) != null) {
                    Class<?> valueType = value.getClass();
                    if (resolveNestedPlaceholders && value instanceof String) {
                        value = resolveNestedPlaceholders((String) value);
                    }
                    if (debugEnabled) {
                        logger.debug(String.format("Found key '%s' in [%s] with type [%s] and value '%s'",
                                key, propertySource.getName(), valueType.getSimpleName(), value));
                    }
//                    if (!this.conversionService.canConvert(valueType, targetValueType)) {
//                        throw new IllegalArgumentException(String.format(
//                                "Cannot convert value [%s] from source type [%s] to target type [%s]",
//                                value, valueType.getSimpleName(), targetValueType.getSimpleName()));
//                    }
//                    return this.conversionService.convert(value, targetValueType);
                    return (T) value;
                }
            }
        }
        if (debugEnabled) {
            logger.debug(String.format("Could not find key '%s' in any property source. Returning [null]", key));
        }
        return null;
    }
}
