package org.smileframework.ioc.bean.core.env;

import java.util.Properties;

/**
 * @author liuxin
 * @version Id: PropertiesPropertySource.java, v 0.1 2018-12-07 17:30
 */
public class PropertiesPropertySource extends PropertySource<Properties> {

  Properties properties;

  public PropertiesPropertySource(String name, Properties source) {
    super(name, source);
    this.properties = source;
  }

  public PropertiesPropertySource(String name) {
    super(name);
  }

  @Override
  public Object getProperty(String name) {
    return properties.get(name);
  }
}
