package org.smileframework.web.server.service;

import io.netty.handler.codec.http.HttpResponseStatus;
import org.slf4j.Logger;
import org.smileframework.tool.json.JsonTools;
import org.smileframework.tool.logmanage.LoggerManager;
import org.smileframework.tool.string.StringTools;
import org.smileframework.web.handler.WebDefinition;
import org.smileframework.web.server.modle.MessageRequest;
import org.smileframework.web.server.modle.MessageResponse;
import org.smileframework.web.util.ControllerUtils;

import java.lang.reflect.Method;
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
        Map<String, Object> headers = messageRequest.getHeaders();
        WebDefinition webDefinition = messageRequest.getWebDefinition();
        messageResponse.setContentType(webDefinition.getProduces());
        Method method = webDefinition.getMethod();
        Object controller = webDefinition.getController();
        String produces = webDefinition.getProduces();
        //FIXME 移除参数中判断,提前获取转换类
        //FIXME 下期优化方案,将arg参数,耗时操作在其他地方
        //将统一操作模块化
        Object[] args=null;
        try {
            args = ControllerUtils.getArgs(method, parameters,headers);
        } catch (Exception e) {
            messageResponse.setHttpResponseStatus(HttpResponseStatus.BAD_REQUEST);
            messageResponse.setError("{\"code\":-1,\"message\":\"" + e.getMessage() + "\"}");
        }
        logger.debug("请求参数:{}", JsonTools.toJson(args));
        if (StringTools.isNotEmpty(messageResponse.getError())) {
            return true;
        }
        Object invokeResult = method.invoke(controller, args);
        String result = "";
        if (produces.equalsIgnoreCase("application/json")) {
            result = JsonTools.toJsonByJackson(invokeResult);
        } else if (produces.equalsIgnoreCase("text/plain")) {
            result = String.valueOf(invokeResult);
        }
        messageResponse.setMessageId(messageRequest.getMessageId());
        messageResponse.setResult(result);
        messageResponse.setHttpResponseStatus(HttpResponseStatus.OK);
        return true;
    }
}
