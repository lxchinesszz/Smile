package org.smileframework.ioc.bean.context.beanfactory.impl.aware;

import org.smileframework.ioc.bean.annotation.SmileComponent;
import org.smileframework.ioc.bean.context.aware.EnvironmentAware;
import org.smileframework.ioc.bean.core.env.Environment;

/**
 *
 * @author liuxin
 * @version Id: BeanFactoryAwaretest.java, v 0.1 2018-12-07 18:15
 */
@SmileComponent
public class BeanFactoryAwaretest implements EnvironmentAware {

  Environment environment;

  @Override
  public void setEnvironment(Environment environment) {
    this.environment = environment;
  }

  public Environment getEnvironment() {
    return environment;
  }
}
