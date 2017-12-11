package org.smileframework.web.context;


import org.smileframework.web.handler.WebHandler;

import java.util.List;

/**
 * @Package: org.smileframework.web.context
 * @Description: web模块上线
 * @author: liuxin
 * @date: 2017/12/4 下午1:53
 */
public interface WebContext {

    /**
     * 根据url获取处理器
     * @param url
     * @return
     */
    WebHandler getHandlerByUrl(String url);

    /**
     * 根据请求方法获取处理器
     * @param reqMethod 请求方法
     * @return
     */
    List<WebHandler> getHandlerByMethod(String reqMethod);

    /**
     * 根据url绑定url
     */
    void bindUrl();
}
