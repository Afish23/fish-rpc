package com.fish.rpc.transmission.netty.client;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.fish.rpc.constant.RpcConstant;
import com.fish.rpc.dto.RpcReq;
import com.fish.rpc.dto.RpcResp;
import com.fish.rpc.transmission.RpcClient;
import com.fish.rpc.transmission.netty.codec.NettyRpcDecode;
import com.fish.rpc.transmission.netty.codec.NettyRpcEncode;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.AttributeKey;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Afish
 * @date 2025/4/21 10:50
 */
@Slf4j
public class NettyRpcClient implements RpcClient {
    private static final Bootstrap bootstrap;
    private static final int DEFAULT_CONNECT_TIMEOUT_MILLIS = 5000;

    static {
        bootstrap = new Bootstrap();
        bootstrap.group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, DEFAULT_CONNECT_TIMEOUT_MILLIS)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        channel.pipeline().addLast(new NettyRpcDecode());
                        channel.pipeline().addLast(new NettyRpcEncode());
                        channel.pipeline().addLast(new NettyRpcClientHandler());
                    }
                });
    }

    @SneakyThrows
    @Override
    public RpcResp<?> sendReq(RpcReq req) {
        ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8888).sync();
        Channel channel = channelFuture.channel();
        String interfaceName = req.getInterfaceName();
        channel.writeAndFlush(interfaceName).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
        //阻塞等待，直到关闭
        channel.closeFuture().sync();
        //获取服务端响应数据
        AttributeKey<String> key = AttributeKey.valueOf(RpcConstant.NETTY_RPC_KEY);
        String s = channel.attr(key).get();
        System.out.println(s);
        return null;
    }
}
