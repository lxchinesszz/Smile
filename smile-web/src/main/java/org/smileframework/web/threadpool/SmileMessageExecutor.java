package org.smileframework.web.threadpool;

import com.google.common.util.concurrent.*;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import org.slf4j.Logger;
import org.smileframework.tool.logmanage.LoggerManager;
import org.smileframework.web.server.modle.MessageRequest;
import org.smileframework.web.server.modle.MessageResponse;
import org.smileframework.web.util.NettyResponse;

import java.util.concurrent.Callable;
import java.util.concurrent.ThreadPoolExecutor;

import static io.netty.handler.codec.http.HttpHeaders.Names.*;

/**
 * @Package: cobra.cobra.core.threadpool
 * @Description: 执行线程
 * @author: liuxin
 * @date: 2017/10/17 下午3:17
 */
public class SmileMessageExecutor {
    private static final Logger logger= LoggerManager.getLogger(SmileMessageExecutor.class);

    /**
     * 维护线程可见性
     */
    private static volatile ListeningExecutorService threadPoolExecutor;


    public static void submit(Callable<Boolean> task, final ChannelHandlerContext ctx, HttpRequest metaRequest, final MessageRequest request, final MessageResponse response) {

        /**
         * SmileThreadFactory 目的构建自己的线程名,并通过线程组进行统一管理
         * SmileThreadPoolExecutor 构建自己的线程池,对任务进行,细微管理
         */
        if (threadPoolExecutor == null) {
            SmileThreadPoolExecutor smileThreadPoolExecutor = new SmileThreadPoolExecutor(new SmileThreadFactory("Smile"));
            ThreadPoolExecutor executorService = (ThreadPoolExecutor) smileThreadPoolExecutor.getExecutory();
            threadPoolExecutor = MoreExecutors.listeningDecorator(executorService);
        }
        /**
         * 处理完成任务如果任务完成就,渲染出去
         */
        ListenableFuture<Boolean> listenableFuture = threadPoolExecutor.submit(task);
        Futures.addCallback(listenableFuture, new FutureCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                if (result){
                    NettyResponse.writeResponseAndListener(ctx.channel(), request, response, new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture channelFuture) throws Exception {
                            channelFuture.channel().close();
                            logger.info("Smile Server Send message-id:{}" , request.getMessageId());
                        }
                    });
                }
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        }, threadPoolExecutor);

    }
}
