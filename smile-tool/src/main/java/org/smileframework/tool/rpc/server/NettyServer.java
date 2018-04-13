package org.smileframework.tool.rpc.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.smileframework.tool.threadpool.SmileThreadFactory;

/**
 * @Package: org.smileframework.tool.rpc.server
 * @Description: ${todo}
 * @author: liuxin
 * @date: 2018/3/29 上午10:10
 */
public class NettyServer {
    public static void start(int port) throws Exception {

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        // 通过nio方式来接收连接和处理连接
        EventLoopGroup group = new NioEventLoopGroup(0, new SmileThreadFactory("game-group"));
        EventLoopGroup work = new NioEventLoopGroup(0, new SmileThreadFactory("game-work"));
        /**
         *  TCP_NODELAY就是用于启用或关于Nagle算法。如果要求高实时性，有数据发送时就马上发送，
         *  就将该选项设置为true关闭Nagle算法；如果要减少发送次数减少网络交互，
         *  就设置为false等累积一定大小后再发送。默认为false。
         */
        serverBootstrap.group(group, work).childOption(ChannelOption.TCP_NODELAY,true).channel(NioServerSocketChannel.class);
        serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel channel) throws Exception {
                ChannelPipeline pipeline = channel.pipeline();
                pipeline.addLast("decode",new StringDecoder());
                pipeline.addLast("encode",new StringEncoder());
                pipeline.addLast("handler", new ChannelInboundHandlerAdapter() {

                    @Override
                    public void channelActive(ChannelHandlerContext ctx) throws Exception {
                        System.out.println("连接成功");
                    }

                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                        System.out.println("收到信息:"+msg);
                        ctx.channel().writeAndFlush("服务端返回信息");
                    }

                    @Override
                    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                        cause.printStackTrace();
                    }
                });
            }
        });
        try {
            ChannelFuture sync = serverBootstrap.bind(port).sync();
            sync.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (channelFuture.isSuccess()) {
                        System.out.println("服务器绑定成功");
                    }
                }
            });
        } catch (InterruptedException e) {
            work.shutdownGracefully();
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args)throws Exception {
        NettyServer.start(10102);
    }
}
