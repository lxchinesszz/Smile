package org.smileframework.ioc.bean.context.beanfactory.impl.bean;

import org.smileframework.ioc.bean.annotation.Autowired;
import org.smileframework.ioc.bean.annotation.SmileComponent;

/**
 * @author liuxin
 * @version Id: Classes.java, v 0.1 2018-12-05 11:51
 */
@SmileComponent
public class Classes {
  private String name = "三年二班";

  @Autowired
  Student student;

  @Autowired
  Teacher teacher;



  @Override
  public String toString() {
    return "Classes{" +
      "name='" + name + '\'' +
      ", student=" + student +
      ", teacher=" + teacher +
      '}';
  }
}
