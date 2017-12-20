package org.smileframework.web.handler;

import lombok.Data;
import org.smileframework.tool.clazz.ReflectionTools;
import org.smileframework.web.annotation.RequestMethod;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @Package: org.smileframework.web.handler
 * @Description: web描述
 * @author: liuxin
 * @date: 2017/12/4 下午7:01
 */
@Data
public class WebDefinition {
    /**
     * 请求地址
     */
    private String reqUrl;
    /**
     * 请求方法
     */
    private RequestMethod value;
    /**
     * 请求处理格式
     */
    private String consumes;
    /**
     * 请求响应格式
     *
     * @return
     */
    private String produces;

    /**
     * 处理类
     */
    private Object controller;
    /**
     * 处理方法
     */
    private Method method;

    /**
     * 处于提高效率,不在编译器解释
     * 在使用的时候在建议
     */
    private String[] parameterNames;
    /**
     * 参数信息,所以名称及注解
     */
    List<ReflectionTools.ParamDefinition> paramDefinitions;

    public WebDefinition(){}

    public WebDefinition(String reqUrl, RequestMethod value, String consumes, String produces, Object controller, Method method, String[]parameterNames) {
        this.reqUrl=reqUrl;
        this.value = value;
        this.consumes = consumes;
        this.produces = produces;
        this.controller = controller;
        this.method = method;
        this.parameterNames=parameterNames;
        paramDefinitions=ReflectionTools.getParameterDefinitions(method);
    }
}
