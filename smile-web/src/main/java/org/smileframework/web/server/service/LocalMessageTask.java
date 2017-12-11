package org.smileframework.web.server.service;

import io.netty.handler.codec.http.HttpResponseStatus;
import org.slf4j.Logger;
import org.smileframework.tool.asserts.Assert;
import org.smileframework.tool.json.JsonUtils;
import org.smileframework.tool.logmanage.LoggerManager;
import org.smileframework.web.handler.WebDefinition;
import org.smileframework.web.server.modle.MessageRequest;
import org.smileframework.web.server.modle.MessageResponse;
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
    private static final Logger logger= LoggerManager.getLogger(LocalMessageTask.class);

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
        Map<String,Object> parameters = messageRequest.getParameters();
        WebDefinition webDefinition = messageRequest.getWebDefinition();
        Method method = webDefinition.getMethod();
        Object controller = webDefinition.getController();
        String produces = webDefinition.getProduces();
        //方法的形参名称 ,需要从查看参数中是否包括这个字段
        String[] parameterNames = LocalVariableTableParameterName.getParameterNames(method);
        Arrays.asList(parameterNames).stream().forEach(x->{
            Object o = parameters.get(x);
            Assert.isNull(o,"请检查:[ "+x+" ]");
        });
        Object invokeResult = method.invoke(controller, parameters);
        String result="";
        if (produces.equalsIgnoreCase("application/json")){
            result=  JsonUtils.toJsonByJackson(invokeResult);
        }
        messageResponse.setMessageId(messageRequest.getMessageId());
        messageResponse.setResult(result);
        messageResponse.setContentType(webDefinition.getConsumes());
        messageResponse.setHttpResponseStatus(HttpResponseStatus.OK);
        return true;
    }
}
