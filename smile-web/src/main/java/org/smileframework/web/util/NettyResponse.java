package org.smileframework.web.util;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.smileframework.web.server.modle.MessageRequest;
import org.smileframework.web.server.modle.MessageResponse;

import static io.netty.buffer.Unpooled.copiedBuffer;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;

/**
 * @Package: rattlesnake.callback.util
 * @Description: 写出不同类型的错误
 * @author: liuxin
 * @date: 17/5/18 下午2:17
 */
public class NettyResponse {
    /**
     * 没有找到默认返回404
     * 默认响应，一般当没有找到服务对应的处理器，会自动默认返回，友好提示
     *
     * @param channel
     * @param responseStr ResonseStatus中对应的提示
     */
    public static void writeResponse(Channel channel, String responseStr, HttpResponseStatus httpResponseStatus) {
        String strVar = responseStr;
        ByteBuf buf = copiedBuffer(strVar.toString(), CharsetUtil.UTF_8);
        //连接结束的时候返回true响应信息
        //构建响应对象
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, httpResponseStatus, buf);
        response.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");
        response.setStatus(httpResponseStatus);
        ChannelFuture future = channel.writeAndFlush(response);
        future.addListener(ChannelFutureListener.CLOSE);
    }


    /**
     * @param channel            当前连接
     * @param responseStr        响应信息
     * @param contentType        响应格式
     * @param httpResponseStatus 返回http状态
     */
    public static void writeResponseAndHeader(Channel channel, String responseStr, String contentType, HttpResponseStatus httpResponseStatus) {
        String strVar = responseStr;
        ByteBuf buf = copiedBuffer(strVar.toString(), CharsetUtil.UTF_8);
        //连接结束的时候返回true响应信息
        //构建响应对象
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, httpResponseStatus, buf);
        response.headers().set(CONTENT_TYPE, contentType);
        response.setStatus(httpResponseStatus);
        ChannelFuture future = channel.writeAndFlush(response);
        future.addListener(ChannelFutureListener.CLOSE);
    }


    /**
     *
     * @param channel
     * @param messageRequest
     * @param messageResponse
     * @param futureListener
     */
    public static void writeResponseAndListener(Channel channel, MessageRequest messageRequest, MessageResponse messageResponse,GenericFutureListener<? extends Future<? super Void>>futureListener ) {
        String strVar = messageResponse.getResult();
        HttpResponseStatus httpResponseStatus = messageResponse.getHttpResponseStatus();
        ByteBuf buf = copiedBuffer(strVar.toString(), CharsetUtil.UTF_8);
        String contentType = messageRequest.getWebDefinition().getConsumes();
        //连接结束的时候返回true响应信息
        //构建响应对象
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, httpResponseStatus, buf);
        response.headers().set(CONTENT_TYPE, contentType);
        response.setStatus(httpResponseStatus);
        ChannelFuture future = channel.writeAndFlush(response);
        future.addListener(ChannelFutureListener.CLOSE);
    }
}
