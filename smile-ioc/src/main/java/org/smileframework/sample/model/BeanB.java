package org.smileframework.sample.model;
/**
 * @Package: pig.boot.ioc.context
 * @Description: 获取参数
 * @author: liuxin
 * @date: 2017/11/17 下午11:55
 */
public class BeanB {
    private String content;


    public BeanB(String context){
        this.content=context;
    }



    public BeanC beanC(){
        return new BeanC("im beanc");
    }

    @Override
    public String toString() {
        return "BeanB{" +
                "content='" + content + '\'' +
                '}';
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
