package org.smileframework.web.server.strategy;

import org.smileframework.web.server.modle.MessageRequest;
import org.smileframework.web.server.modle.MessageResponse;
import org.smileframework.web.server.service.LocalMessageTask;
import org.smileframework.web.server.service.RpcProcessTask;

import java.util.concurrent.Callable;

/**
 * @Package: org.smileframework.web.server.strategy
 * @Description: 处理器选择
 * @author: liuxin
 * @date: 2017/12/5 下午3:18
 */
public class DefaultTaskProcessChoice implements SmileTaskChoice {
    private boolean isRpc = false;
    private MessageRequest messageRequest;
    private MessageResponse messageResponse;

    /**
     *
     * @param request
     * @param response
     * @param isRpc 本地方法:false rpc调用:true
     */
    public DefaultTaskProcessChoice(final MessageRequest request, final MessageResponse response, boolean isRpc) {
        this.messageRequest = request;
        this.messageResponse = response;
        this.isRpc = isRpc;
    }

    @Override
    public Callable choice() {
        Callable callTask =null;
        if (!isRpc) {
            callTask = new LocalMessageTask(messageRequest, messageResponse);
        }else {
            callTask=new RpcProcessTask(messageRequest,messageResponse);
        }
        return callTask;
    }
}
