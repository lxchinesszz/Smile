package org.smileframework.web.server.service;



import org.smileframework.web.server.modle.MessageRequest;
import org.smileframework.web.server.modle.MessageResponse;

import java.util.concurrent.Callable;

/**
 * @Package: cobra.cobra.service.task
 * @Description: 执行任务
 * @author: liuxin
 * @date: 2017/10/17 下午3:11
 */
public class RpcProcessTask implements Callable<Boolean> {
    private Boolean ERROR = false;
    private MessageRequest messageRequest;
    private MessageResponse messageResponse;


    public RpcProcessTask(MessageRequest messageRequest, MessageResponse messageResponse) {
        this.messageRequest = messageRequest;
        this.messageResponse = messageResponse;
    }

    /**
     * 异步请求
     * @return
     * @throws Exception
     */
    @Override
    public Boolean call() throws Exception {
        //TODO 处理MessageRequest
//        String url = MapUtils.getString(messageRequest.getParameters(), "url");
//        String methodName = messageRequest.getMethodName();
//        Map parameters = messageRequest.getParameters();
//        String result = null;
//        try {
//            result = HttpClients.httpRequest(methodName, url, parameters);
//        } catch (Exception e) {
//            e.printStackTrace();
//            messageResponse.setError(e.getMessage());
//            return ERROR;
//        }
//        messageResponse.setResult(result);
//        //TODO 根据处理结果拼装MessageResponse
//        messageResponse.setMessageId(messageRequest.getMessageId());
        return true;
    }
}
