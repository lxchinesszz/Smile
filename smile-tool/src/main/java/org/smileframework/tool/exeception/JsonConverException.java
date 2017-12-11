package org.smileframework.tool.exeception;

/**
 * @Package: elephant.zybank.config.exception
 * @Description: 当从其他服务获取订单json准换错误时候，抛出
 * @author: liuxin
 * @date: 17/6/13 上午11:23
 */
public class JsonConverException extends RuntimeException {
    public JsonConverException() {
    }

    public JsonConverException(String msg) {
        super(msg);
    }
}
