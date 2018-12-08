package org.smileframework.ioc.bean.context.beanfactory.impl.bean;

import org.smileframework.ioc.bean.annotation.SmileComponent;
import org.smileframework.ioc.bean.annotation.Value;

/**
 * @author liuxin
 * @version Id: Teacher.java, v 0.1 2018-12-05 13:51
 */
@SmileComponent
public class Teacher {

  @Value("${teacher_name}")
  private String name;

  @Override
  public String toString() {
    return "Teacher{" +
      "name='" + name + '\'' +
      '}';
  }
}
