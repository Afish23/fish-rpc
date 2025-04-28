package com.fish.rpc.transmission.netty.client;

import com.fish.rpc.constant.RpcConstant;
import com.fish.rpc.dto.RpcMsg;
import com.fish.rpc.dto.RpcResp;
import com.fish.rpc.enums.CompressType;
import com.fish.rpc.enums.MsgType;
import com.fish.rpc.enums.SerializeType;
import com.fish.rpc.enums.VersionType;
import com.fish.rpc.util.ConfigUtils;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Afish
 * @date 2025/4/21 15:56
 */
@Slf4j
public class NettyRpcClientHandler extends SimpleChannelInboundHandler<RpcMsg> {
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("客户端发生异常", cause);
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcMsg rpcMsg) throws Exception {
        if (rpcMsg.getMsgType().isHeartbeat()){
            log.debug("收到服务端心跳：{}", rpcMsg);
            return;
        }
        log.debug("收到服务端数据：{}", rpcMsg);
        RpcResp<?> rpcResp = (RpcResp<?>) rpcMsg.getData();
        UnprocessedRpcReq.complete(rpcResp);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        boolean isNeedHeartBeat = evt instanceof IdleStateEvent && ((IdleStateEvent) evt).state() == IdleState.WRITER_IDLE;
        if (!isNeedHeartBeat) {
            super.userEventTriggered(ctx, evt);
            return;
        }
        String serializer = ConfigUtils.getRpcConfig().getSerializer();
        RpcMsg rpcMsg = RpcMsg.builder()
                .version(VersionType.VERSION1)
                .serializeType(SerializeType.from(serializer))
                .compressType(CompressType.GZIP)
                .msgType(MsgType.HEARTBEAT_REQ)
                .build();
        log.debug("客户端发送心跳, {}", rpcMsg);
        ctx.writeAndFlush(rpcMsg).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
    }
}
