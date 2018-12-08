package org.smileframework.ioc.bean.context.beanfactory.impl.bean.interfaces;

import org.smileframework.ioc.bean.annotation.SmileComponent;
import org.smileframework.ioc.bean.annotation.Value;

/**
 * @author liuxin
 * @version Id: Tool.java, v 0.1 2018-12-05 18:44
 */
@SmileComponent
public class Tool {

  @Value("${tool}")
  private String name;

  @Override
  public String toString() {
    return "Tool{" +
      "name='" + name + '\'' +
      '}';
  }
}
