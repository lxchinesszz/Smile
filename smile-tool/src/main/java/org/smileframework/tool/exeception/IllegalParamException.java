package org.smileframework.tool.exeception;

/**
 * @Package: elephant.zybank.config.exception
 * @Description: 当出现参数非法时候返回
 * @author: liuxin
 * @date: 17/6/13 上午11:23
 */
public class IllegalParamException extends RuntimeException {
    public IllegalParamException() {
    }

    public IllegalParamException(String msg) {
        super(msg);
    }
}
