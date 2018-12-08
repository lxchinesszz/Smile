package org.smileframework.ioc.bean.context.beandefinition;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.List;

/**
 * @author liuxin
 * @version Id: ConstructorInfo.java, v 0.1 2018-12-04 17:24
 */
public class ConstructorInfo {
  /**
   * 构造的参数信息
   */
  ConstructorArgumentValues constructorArgumentValues;
  /**
   * 构造上注解
   */
  List<Annotation> declaredAnnotations;

  /**
   * 原始构造
   */
  Constructor originalConstructor;

  public ConstructorArgumentValues getConstructorArgumentValues() {
    return constructorArgumentValues;
  }

  public void setConstructorArgumentValues(ConstructorArgumentValues constructorArgumentValues) {
    this.constructorArgumentValues = constructorArgumentValues;
  }

  public List<Annotation> getDeclaredAnnotations() {
    return declaredAnnotations;
  }

  public void setDeclaredAnnotations(List<Annotation> declaredAnnotations) {
    this.declaredAnnotations = declaredAnnotations;
  }

  public Constructor getOriginalConstructor() {
    return originalConstructor;
  }

  public void setOriginalConstructor(Constructor originalConstructor) {
    this.originalConstructor = originalConstructor;
  }

  @Override
  public String toString() {
    return "ConstructorInfo{" +
      "constructorArgumentValues=" + constructorArgumentValues +
      ", declaredAnnotations=" + declaredAnnotations +
      ", originalConstructor=" + originalConstructor +
      '}';
  }
}
