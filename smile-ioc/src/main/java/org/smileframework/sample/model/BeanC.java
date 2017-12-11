package org.smileframework.sample.model;

/**
 * @Package: pig.boot.ioc.context
 * @Description: 获取参数
 * @author: liuxin
 * @date: 2017/11/17 下午11:55
 */
public class BeanC {
    private String content;

   public BeanC(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "BeanC.content = " + content;
    }

}
