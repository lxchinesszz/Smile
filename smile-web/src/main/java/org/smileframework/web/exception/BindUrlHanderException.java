package org.smileframework.web.exception;

/**
 * @Package: org.smileframework.web.exception
 * @Description: 绑定url处理异常
 * @author: liuxin
 * @date: 2017/12/4 下午6:05
 */
public class BindUrlHanderException extends RuntimeException {
   public BindUrlHanderException(String message) {
        super(message);
    }


   public BindUrlHanderException(String message,Throwable throwable) {
        super(message,throwable);
    }

}
