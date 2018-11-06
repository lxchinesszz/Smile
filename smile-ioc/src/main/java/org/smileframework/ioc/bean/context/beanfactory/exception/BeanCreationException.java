package org.smileframework.ioc.bean.context.beanfactory.exception;

/**
 * @author liuxin
 * @version Id: BeanCreationException.java, v 0.1 2018/10/30 10:11 AM
 */
public class BeanCreationException extends RuntimeException {
    public BeanCreationException(String beanName,String msg){
        super(beanName+":"+msg);
    }

    public BeanCreationException(String msg,Throwable throwable){
        super(msg,throwable);
    }

    public BeanCreationException(String beanName,String msg,Throwable throwable){

        super(beanName+":"+msg,throwable);
    }
}
