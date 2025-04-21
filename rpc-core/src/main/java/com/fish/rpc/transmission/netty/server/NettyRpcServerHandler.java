package com.fish.rpc.transmission.netty.server;

import com.fish.rpc.dto.RpcReq;
import com.fish.rpc.dto.RpcResp;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Afish
 * @date 2025/4/21 16:12
 */
@Slf4j
public class NettyRpcServerHandler extends SimpleChannelInboundHandler<String> {
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("服务端发生异常", cause);
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String str) throws Exception {
        log.debug("收到客户端请求：{}", str);
        //RpcResp<String> rpcResp = RpcResp.success(str, "模拟响应数据");
        ctx.channel().writeAndFlush("模拟响应数据")
                .addListener(ChannelFutureListener.CLOSE);
    }

}
