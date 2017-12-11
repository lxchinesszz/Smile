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
@Data
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


}


