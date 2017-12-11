package org.smileframework.web.util;

import org.smileframework.tool.string.StringTools;
import org.smileframework.web.annotation.RequestMethod;
import org.smileframework.web.handler.WebHandler;

import java.lang.reflect.Method;

/**
 * @Package: org.smileframework.web.util
 * @Description:
 * @author: liuxin
 * @date: 2017/12/4 下午1:38
 */
public class WebTools {


    public static WebHandler getHandlerByUrl(String url) {
        return null;
    }

    public static void checkRequestArgs(Method method) {
        String[] parameterNames = LocalVariableTableParameterName.getParameterNames(method);
        return;
    }

    public static String[] getParameterNames(Method method) {
        return LocalVariableTableParameterName.getParameterNames(method);
    }

    public static String checkUrl(String oneUrl, String twoUrl) {
        if (StringTools.isNotEmpty(oneUrl)) {
            //FIXME 避免出现a//b 这样的错误url
            if (oneUrl.endsWith("/") & twoUrl.startsWith("/")) {
                twoUrl = twoUrl.substring(1, twoUrl.length());
            }

        } else if (!twoUrl.startsWith("/")) {
            //FIXME 给二级url添加前缀/
            twoUrl = "/" + twoUrl;
        }
        return twoUrl;
    }


    public static RequestMethod getRequestMethod(String methodName) {
        methodName = methodName.toUpperCase();
        switch (methodName) {
            case "GET":
                return RequestMethod.GET;
            case "POST":
                return RequestMethod.POST;
            default:
                return RequestMethod.GET;
        }
    }
}

