package org.smileframework.web.server.modle;

import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.Data;

import java.io.Serializable;

/**
 * @Package: cobra.cobra.modle
 * @Description:
 * @author: liuxin
 * @date: 2017/10/9 下午7:42
 */
@Data
public class MessageResponse implements Serializable {
    /**
     * 响应id=请求id
     */
    private String messageId;
    /**
     * 是否失败
     */
    private String error;
    /**
     * 正常返回
     */
    private String result;

    /**
     * 响应
     */
    private String contentType;

    /**
     * http状态
     */
    private HttpResponseStatus httpResponseStatus;

    public MessageResponse(){}

    public MessageResponse(String messageId, String error, String result) {
        this.messageId = messageId;
        this.error = error;
        this.result = result;
    }

    @Override
    public String toString() {
        return "MessageResponse{" +
                "messageId='" + messageId + '\'' +
                ", error='" + error + '\'' +
                ", result=" + result +
                '}';
    }
}
