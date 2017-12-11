package org.smileframework.web.server.netty;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.smileframework.tool.date.StopWatch;
import org.smileframework.tool.logmanage.LoggerManager;
import org.smileframework.web.server.dispatch.HttpDispatchServerHandler;
import org.smileframework.web.threadpool.SmileThreadFactory;

import java.util.concurrent.TimeUnit;

/**
 * @Package: org.smileframework.web.server.netty
 * @Description: netty处理器核心
 * @author: liuxin
 * @date: 2017/12/4 下午10:37
 */
public class NettyBootstrapServer {
    private static final Logger logger = LoggerManager.getLogger(NettyBootstrapServer.class);
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;


    public void bindPort(Integer port, String pid, final StopWatch stopWatch) {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bossGroup = new NioEventLoopGroup(0,new SmileThreadFactory("Smile"));
        workerGroup = new NioEventLoopGroup();
        bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                .localAddress(port).option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                .childOption(ChannelOption.SO_KEEPALIVE, true).childHandler(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel channel) throws Exception {
                ChannelPipeline cp = channel.pipeline();
                cp.addLast("idleStateHandler", new IdleStateHandler(5, 5, 5, TimeUnit.SECONDS));
                cp.addLast("decoder", new HttpRequestDecoder());
                cp.addLast("encoder", new HttpResponseEncoder());
                cp.addLast("aggregator", new HttpObjectAggregator(1048576));
                //对content进行压缩
                cp.addLast("deflater", new HttpContentCompressor());
                cp.addLast("handler", new HttpDispatchServerHandler());
                cp.addLast("out", new AcceptorIdleStateTrigger());
            }
        }).option(ChannelOption.SO_BACKLOG, 128);
        try {
            ChannelFuture channelFuture = bootstrap.bind().addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    boolean isSuccess = channelFuture.isSuccess();
                    if (isSuccess) {
                        showBanner(port,pid,stopWatch);
                    } else {
                        logger.info("绑定端口号:{},失败", port);
                    }
                }
            });
            channelFuture.channel().closeFuture().sync().addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    logger.info("服务已经关闭...");
                }
            });
        } catch (Exception ie) {
            throw new RuntimeException(ie);
        }finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }


    private static void showBanner(Integer port,String pid,final StopWatch stopWatch) {
        logger.info("==========================================================================");
        stopWatch.stop();
        logger.info("Smile Start the time-consuming:{}ms,port:{},pid:{}", stopWatch.getLastTaskTimeMillis(),port,pid);
//        logger.info("===============SmileNettyAppaction已经启动,端口号:" + port + " PID:"+pid+"============");
        logger.info("==========================================================================");
    }

}
