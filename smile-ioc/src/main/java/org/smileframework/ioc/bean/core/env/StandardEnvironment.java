package org.smileframework.ioc.bean.core.env;

import java.util.Map;

/**
 * 在IOC生成BeanDefinition后,开始加载标准环境
 * @author liuxin
 * @version Id: StandardEnvironment.java, v 0.1 2018/10/17 4:16 PM
 */
public class StandardEnvironment extends AbstractEnvironment {
    /**
     * System environment property source name: {@value}
     */

    public static final String SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME = "systemEnvironment";

    /**
     * JVM system properties property source name: {@value}
     */
    public static final String SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME = "systemProperties";

    @Override
    protected void customizePropertySources(MutablePropertySources propertySources) {
        propertySources.add(new CommonProperty(SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME, getSystemProperties()));
        propertySources.add(new CommonProperty(SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME, getSystemEnvironment()));
    }


    public class CommonProperty extends PropertySource {

        Map<String, Object> properties;

        public CommonProperty(String name, Object source) {
            super(name, source);
            properties = ( Map<String, Object>)source;
        }

        @Override
        public Object getProperty(String name) {
            return properties.get(name);
        }
    }
}
