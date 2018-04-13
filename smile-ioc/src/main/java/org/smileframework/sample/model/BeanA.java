package org.smileframework.sample.model;

import org.smileframework.ioc.bean.annotation.InsertBean;
import org.smileframework.ioc.bean.annotation.SmileBean;
import org.smileframework.ioc.bean.annotation.SmileComponent;


/**
 * @Package: pig.boot.ioc.context
 * @Description: 获取参数
 * @author: liuxin
 * @date: 2017/11/17 下午11:55
 */
@SmileComponent
public class BeanA {
    private String content;

    @InsertBean
    private BeanB beanb;

    public BeanA() {
    }

    public BeanA(String content) {
        this.content = content;
    }

    @SmileBean
    public BeanB beanB() {
        return new BeanB("hi. iam is beanB X");
    }

    @SmileBean
    public BeanB beanBX(BeanB b){
        b.setContent("XXXX");
        return b;
    }

}
