package org.smileframework.web.server.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.smileframework.tool.logmanage.LoggerManager;
import org.smileframework.web.util.NettyResponse;

/**
 * @Package: rattlesnake.callback.server.dispatch
 * @Description: 处理超时，输出超时信息408
 * @author: liuxin
 * @date: 17/5/18 下午2:05
 */
@ChannelHandler.Sharable
public class AcceptorIdleStateTrigger extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerManager.getLogger(AcceptorIdleStateTrigger.class);

    private static final ByteBuf HEARTBEAT_SEQUENCE = Unpooled
            .unreleasableBuffer(Unpooled.copiedBuffer("Heartbeat",
                    CharsetUtil.UTF_8));

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
            throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            String type = "";
            if (event.state() == IdleState.READER_IDLE) {
                type = "read idle";
            } else if (event.state() == IdleState.WRITER_IDLE) {
                type = "write idle";
            } else if (event.state() == IdleState.ALL_IDLE) {
                type = "all idle";
            }
            NettyResponse.writeResponseAndHeader(ctx.channel(), "{\"code\":20000,\"message\":\"请求超时\"}","application/json", HttpResponseStatus.REQUEST_TIMEOUT);
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
