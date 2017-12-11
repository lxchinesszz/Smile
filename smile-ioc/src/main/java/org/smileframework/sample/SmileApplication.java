package org.smileframework.sample;


import org.smileframework.ioc.bean.annotation.SmileBootApplication;
import org.smileframework.ioc.bean.context.ConfigurableApplicationContext;
import org.smileframework.ioc.bean.context.SmileApplicationContext;
import org.smileframework.sample.model.BeanA;
import org.smileframework.sample.model.BeanB;

@SmileBootApplication
public class SmileApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = org.smileframework.ioc.bean.context.SmileApplication.run(SmileApplication.class, args);
        System.out.println(run.getBean(BeanB.class).toString());
        System.out.println(run.getBean(BeanA.class).beanB().toString());
    }
}