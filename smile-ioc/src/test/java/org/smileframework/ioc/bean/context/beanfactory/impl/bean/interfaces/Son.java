package org.smileframework.ioc.bean.context.beanfactory.impl.bean.interfaces;

import org.smileframework.ioc.bean.annotation.SmileComponent;
import org.smileframework.ioc.bean.annotation.Value;
import org.smileframework.tool.debug.Console;

/**
 * @author liuxin
 * @version Id: Son.java, v 0.1 2018-12-05 17:56
 */
@SmileComponent
public class Son extends Person{

  @Value("${say_text}")
  private String text;

  @Value("${age}")
  private int age;


  @Override
  public void say() {
    Console.customerAbnormal("说话",text);
  }

  @Override
  public String toString() {
    return "Son{" +
      "text='" + text + '\'' +
      ", age=" + age +
      '}';
  }
}
