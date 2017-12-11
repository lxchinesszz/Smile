package org.smileframework.web.annotation;

/**
 * @Package: org.smileframework.web.annotation
 * @Description: 处理类
 * @author: liuxin
 * @date: 2017/12/4 下午7:06
 */
public enum RequestMethod {
    GET("get"), POST("post"), PUT("put");

    public String value;

    RequestMethod(String method) {
        this.value = method;
    }

    public String vlaue(){
        return this.value;
    }
}
