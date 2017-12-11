package org.smileframework.web.util;

import org.smileframework.web.annotation.RequestMethod;
import org.smileframework.web.context.WebApplicationContext;
import org.smileframework.web.handler.WebDefinition;

/**
 * @Package: org.smileframework.web.util
 * @Description: 当前web上下文
 * @author: liuxin
 * @date: 2017/12/5 下午5:14
 */
public class WebContextTools {

    private static WebApplicationContext webApplicationContext;

    public static void setWebApplicationContext(WebApplicationContext context) {
        webApplicationContext = context;
    }

    public static WebDefinition getWebDefinitionByUrl(String url, RequestMethod requestMethod) {
        return webApplicationContext.getWebDefinitionByUrl(url, requestMethod);
    }
}
