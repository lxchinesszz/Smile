package org.smileframework.web.server.modle;

import lombok.Data;
import org.smileframework.web.annotation.RequestMethod;
import org.smileframework.web.handler.WebDefinition;

import java.io.Serializable;
import java.util.Map;

/**
 * @Package: cobra.cobra.modle
 * @Description: 构建请求url地址
 * @author: liuxin
 * @date: 2017/10/9 下午7:41
 */
public class MessageRequest implements Serializable {
    /**
     * 随机分配请求id
     */
    private String messageId;
    /**
     * 请求方法get | post ...
     */
    private RequestMethod methodName;
    /**
     *
     */
    private Map parameters;

    /**
     * 当前请求处理的web描述
     */
    private WebDefinition webDefinition;

    /**
     * 请求头
     */
   private Map<String, Object> headers;

    public MessageRequest(String messageId, RequestMethod methodName, Map parameters, WebDefinition webDefinition, Map headers){
        this.messageId=messageId;
        this.methodName=methodName;
        this.parameters=parameters;
        this.webDefinition=webDefinition;
        this.headers=headers;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public RequestMethod getMethodName() {
        return methodName;
    }

    public void setMethodName(RequestMethod methodName) {
        this.methodName = methodName;
    }

    public Map getParameters() {
        return parameters;
    }

    public void setParameters(Map parameters) {
        this.parameters = parameters;
    }

    public WebDefinition getWebDefinition() {
        return webDefinition;
    }

    public void setWebDefinition(WebDefinition webDefinition) {
        this.webDefinition = webDefinition;
    }

    public Map<String, Object> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, Object> headers) {
        this.headers = headers;
    }
}


