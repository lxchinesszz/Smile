package org.smileframework.web.server.service;

import io.netty.handler.codec.http.HttpResponseStatus;
import org.slf4j.Logger;
import org.smileframework.tool.asserts.Assert;
import org.smileframework.tool.json.JsonUtils;
import org.smileframework.tool.logmanage.LoggerManager;
import org.smileframework.tool.string.StringTools;
import org.smileframework.web.handler.WebDefinition;
import org.smileframework.web.server.modle.MessageRequest;
import org.smileframework.web.server.modle.MessageResponse;
import org.smileframework.web.util.ControllerUtils;
import org.smileframework.web.util.LocalVariableTableParameterName;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @Package: org.smileframework.web.server.service
 * @Description: 本地处理器
 * @author: liuxin
 * @date: 2017/12/5 下午2:54
 */
public class LocalMessageTask implements Callable<Boolean> {
    private static final Logger logger = LoggerManager.getLogger(LocalMessageTask.class);

    private Boolean ERROR = false;
    private MessageRequest messageRequest;
    private MessageResponse messageResponse;


    public LocalMessageTask(MessageRequest messageRequest, MessageResponse messageResponse) {
        this.messageRequest = messageRequest;
        this.messageResponse = messageResponse;
    }


    /**
     * 本地反射bean处理处理
     *
     * @return
     * @throws Exception
     */
    @Override
    public Boolean call() throws Exception {
        Map<String, Object> parameters = messageRequest.getParameters();
        WebDefinition webDefinition = messageRequest.getWebDefinition();
        messageResponse.setContentType(webDefinition.getProduces());
        Method method = webDefinition.getMethod();
        Object controller = webDefinition.getController();
        String produces = webDefinition.getProduces();
        //方法的形参名称 ,需要从查看参数中是否包括这个字段
        String[] parameterNames = LocalVariableTableParameterName.getParameterNames(method);
        Arrays.stream(parameterNames).forEach(paramKey -> {
            Object paramValue = parameters.get(paramKey);
            if (paramValue == null) {
                messageResponse.setHttpResponseStatus(HttpResponseStatus.BAD_REQUEST);
                messageResponse.setError("{\"code\":-1,\"message\":\"缺少请求参数:" + paramKey + "\"}");
            }
        });
        if (StringTools.isNotEmpty(messageResponse.getError())) {
            return true;
        }
        Object[] args = ControllerUtils.getArgs(method, parameters);
        logger.debug("请求参数:{}", JsonUtils.toJson(args));
        Object invokeResult = method.invoke(controller, args);
        String result = "";
        if (produces.equalsIgnoreCase("application/json")) {
            result = JsonUtils.toJsonByJackson(invokeResult);
        } else if (produces.equalsIgnoreCase("text/plain")) {
            result = String.valueOf(invokeResult);
        }
        messageResponse.setMessageId(messageRequest.getMessageId());
        messageResponse.setResult(result);
        messageResponse.setHttpResponseStatus(HttpResponseStatus.OK);
        return true;
    }
}
