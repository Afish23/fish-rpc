package com.fish.rpc.transmission.netty.server;

import com.fish.rpc.dto.RpcMsg;
import com.fish.rpc.dto.RpcReq;
import com.fish.rpc.dto.RpcResp;
import com.fish.rpc.enums.CompressType;
import com.fish.rpc.enums.MsgType;
import com.fish.rpc.enums.SerializeType;
import com.fish.rpc.enums.VersionType;
import com.fish.rpc.factory.SingletonFactory;
import com.fish.rpc.handler.RpcReqHandler;
import com.fish.rpc.provider.ServiceProvider;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Afish
 * @date 2025/4/21 16:12
 */
@Slf4j
public class NettyRpcServerHandler extends SimpleChannelInboundHandler<RpcMsg> {
    private final RpcReqHandler rpcReqHandler;

    public NettyRpcServerHandler(ServiceProvider serviceProvider) {
        this.rpcReqHandler = new RpcReqHandler(serviceProvider);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("服务端发生异常", cause);
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcMsg rpcMsg) throws Exception {
        log.debug("收到客户端请求：{}", rpcMsg);

        MsgType msgType;
        Object data;
        if (rpcMsg.getMsgType().isHeartbeat()){
            msgType = MsgType.HEARTBEAT_RESP;
            data = null;
        }else {
            msgType = MsgType.RPC_RESP;
            RpcReq rpcReq = (RpcReq) rpcMsg.getData();
            data = handleRpcReq(rpcReq);
        }

        RpcMsg msg = RpcMsg.builder()
                .reqId(rpcMsg.getReqId())
                .version(VersionType.VERSION1)
                .msgType(msgType)
                .compressType(CompressType.GZIP)
                .serializeType(SerializeType.KRYO)
                .data(data)
                .build();
        ctx.channel().writeAndFlush(msg)
                .addListener(ChannelFutureListener.CLOSE);
    }

    private RpcResp<?> handleRpcReq(RpcReq rpcReq) {
        try {
            Object object = rpcReqHandler.invoke(rpcReq);
            return RpcResp.success(rpcReq.getReqId(), object);
        } catch (Exception e) {
            log.info("调用失败, ", e);
            return RpcResp.fail(rpcReq.getReqId(), e.getMessage());
        }
    }
}
