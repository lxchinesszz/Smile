package org.smileframework.ioc.bean.context.beanfactory.impl.bean.interfaces;

import org.smileframework.ioc.bean.annotation.Autowired;
import org.smileframework.ioc.bean.annotation.SmileComponent;

/**
 * @author liuxin
 * @version Id: Home.java, v 0.1 2018-12-05 18:25
 */
@SmileComponent
public class Home {

  @Autowired
  Son son;


  @Autowired
  Person person;


  @Override
  public String toString() {
    son.say2();
    person.say();
    return "Home{" +
      "son=" + son +
      '}';
  }
}
