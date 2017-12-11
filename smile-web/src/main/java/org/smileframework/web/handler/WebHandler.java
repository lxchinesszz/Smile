package org.smileframework.web.handler;

import io.netty.handler.codec.http.HttpRequest;

/**
 * @Package: org.smileframework.web.handler
 * @Description:
 * @author: liuxin
 * @date: 2017/12/4 下午1:42
 */
public interface WebHandler {
    /**
     * 做一些check 校验,参数类型
     *
     * @param webStrategy
     * @param request
     * @return
     */
    Object doResult(WebStrategy webStrategy, HttpRequest request);
}
