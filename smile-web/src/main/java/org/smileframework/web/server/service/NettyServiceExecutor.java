package org.smileframework.web.server.service;

import com.google.common.util.concurrent.*;
import com.google.gson.Gson;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.smileframework.web.server.modle.MessageRequest;
import org.smileframework.web.server.modle.MessageResponse;
import org.smileframework.web.threadpool.SmileThreadFactory;
import org.smileframework.web.threadpool.SmileThreadPoolExecutor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadPoolExecutor;

import static io.netty.buffer.Unpooled.copiedBuffer;
import static io.netty.handler.codec.http.HttpHeaders.Names.*;

/**
 * @Package: org.smileframework.web.server.service
 * @Description: 执行线程
 * @author: liuxin
 * @date: 2017/12/5 下午2:23
 */
public class NettyServiceExecutor {

    /**
     * 维护线程可见性
     */
    private static volatile ListeningExecutorService threadPoolExecutor;



    public static void submit(Callable<Boolean> task, final ChannelHandlerContext ctx, HttpRequest metaRequest, final MessageRequest messageRequest, final MessageResponse messageResponse) {
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
                Map<String, Object> resultMap = new HashMap<>();
                resultMap.put("data", messageResponse.getResult());
                resultMap.put("error", messageResponse.getError());
                String resultJson = new Gson().toJson(resultMap);
                ByteBuf buf = copiedBuffer(resultJson, CharsetUtil.UTF_8);
                boolean close = metaRequest.headers().contains(CONNECTION, HttpHeaders.Values.CLOSE, true)
                        || metaRequest.getProtocolVersion().equals(HttpVersion.HTTP_1_0)
                        && !metaRequest.headers().contains(CONNECTION, HttpHeaders.Values.KEEP_ALIVE, true);
                //构建响应对象
                FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);
                response.headers().set(CONTENT_TYPE, "application/json; charset=UTF-8");
                if (!close) {
                    //构建内容长度
                    response.headers().set(CONTENT_LENGTH, buf.readableBytes());
                }
                Set<Cookie> cookies;
                String value = metaRequest.headers().get(COOKIE);
                if (value == null) {
                    cookies = Collections.emptySet();
                } else {
                    cookies = CookieDecoder.decode(value);
                }
                if (!cookies.isEmpty()) {
                    // Reset the cookies if necessary.
                    for (Cookie cookie : cookies) {
                        response.headers().add(SET_COOKIE, ServerCookieEncoder.encode(cookie));
                    }
                }
                ctx.writeAndFlush(response).addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture channelFuture) throws Exception {
                        System.out.println("Smile Server Send message-id respone:" + messageRequest.getMessageId());
                    }
                });
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        }, threadPoolExecutor);
    }
}
