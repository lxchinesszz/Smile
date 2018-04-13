package org.smileframework.tool.rpc.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;


import java.net.InetSocketAddress;

/**
 * @Package: org.smileframework.tool.rpc.client
 * @Description:
 * @author: liuxin
 * @date: 2018/3/28 下午10:51
 */
public class NettyClient {
    private final String host;
    private final int port;
    private byte[] result;
    private byte[] request;
    private boolean isProccess = false;

    public NettyClient(String host, int port, byte[] request) {
        this.host = host;
        this.port = port;
        this.request = request;
    }

    public void start() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host, port))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new StringDecoder());
                            pipeline.addLast(new StringEncoder());
                            pipeline.addLast(new SimpleChannelInboundHandler<String>() {
                                //在连接的时候将数据发送给
                                @Override
                                public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                    System.out.println("客户端连接成功");
                                    ctx.channel().writeAndFlush(new String(request));
                                }

                                //接受信息
                                @Override
                                protected void messageReceived(ChannelHandlerContext channelHandlerContext, String byteBuf) throws Exception {
                                    channelHandlerContext.channel().writeAndFlush(request);
                                    result =  byteBuf.getBytes();
                                    setProccess(true);
                                }
                            });
                        }
                    });
            ChannelFuture future = bootstrap.connect().sync();
            future.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    while (channelFuture.isSuccess()&&result!=null){
                        channelFuture.channel().close();
                    }
                }
            });
        } finally {
            group.shutdownGracefully().sync();
        }
    }

    public byte[] getResult() {
        if (!isProccess) {
            try {
                synchronized (this) {
                    this.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public void setProccess(boolean proccess) {
        isProccess = proccess;
        synchronized (this) {
            this.notifyAll();
        }
    }

    public static void main(String[] args) throws Exception {
//        NettyClient nettyClient = new NettyClient("127.0.0.1", 10101, "你好".getBytes());
//        nettyClient.start();
        test();

    }

    public static void test() throws Exception{
        NettyClient nettyClient = new NettyClient("127.0.0.1", 10102, "你好".getBytes());
        nettyClient.start();
        byte[] result = nettyClient.getResult();
        System.out.println(new String(result));
    }
}

