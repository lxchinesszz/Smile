package org.smileframework.ioc.bean.context.beanfactory.impl.bean.interfaces;

import org.smileframework.ioc.bean.annotation.Autowired;
import org.smileframework.ioc.bean.annotation.SmileComponent;

/**
 * @author liuxin
 * @version Id: Father.java, v 0.1 2018-12-05 17:55
 */

public abstract class Person {
  @Autowired
  Tool tool;

  abstract void say();

  public void say2() {
    System.err.println("抽象类实现" + tool.toString());
  }
}
