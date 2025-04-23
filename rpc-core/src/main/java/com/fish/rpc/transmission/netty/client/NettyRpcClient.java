package com.fish.rpc.transmission.netty.client;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.fish.rpc.constant.RpcConstant;
import com.fish.rpc.dto.RpcMsg;
import com.fish.rpc.dto.RpcReq;
import com.fish.rpc.dto.RpcResp;
import com.fish.rpc.enums.CompressType;
import com.fish.rpc.enums.MsgType;
import com.fish.rpc.enums.SerializeType;
import com.fish.rpc.enums.VersionType;
import com.fish.rpc.factory.SingletonFactory;
import com.fish.rpc.registry.ServiceDiscovery;
import com.fish.rpc.registry.impl.ZkServiceDiscovery;
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

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Afish
 * @date 2025/4/21 10:50
 */
@Slf4j
public class NettyRpcClient implements RpcClient {
    private static final Bootstrap bootstrap;
    private static final int DEFAULT_CONNECT_TIMEOUT_MILLIS = 5000;
    private static final AtomicInteger ID_GEN = new AtomicInteger(0);

    private final ServiceDiscovery serviceDiscovery;

    public NettyRpcClient() {
        this(SingletonFactory.getInstance(ZkServiceDiscovery.class));
    }

    public NettyRpcClient(ServiceDiscovery serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery;
    }

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
        InetSocketAddress address = serviceDiscovery.lookupService(req);
        ChannelFuture channelFuture = bootstrap.connect(address).sync();
        log.info("nettyRpcClient连接到: {}", address);
        Channel channel = channelFuture.channel();
        RpcMsg rpcMsg = RpcMsg.builder()
                .reqId(ID_GEN.incrementAndGet())
                .version(VersionType.VERSION1)
                .serializeType(SerializeType.KRYO)
                .compressType(CompressType.GZIP)
                .msgType(MsgType.RPC_REQ)
                .data(req)
                .build();
        channel.writeAndFlush(rpcMsg).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
        //阻塞等待，直到关闭
        channel.closeFuture().sync();
        //获取服务端响应数据
        AttributeKey<RpcResp<?>> key = AttributeKey.valueOf(RpcConstant.NETTY_RPC_KEY);
        return channel.attr(key).get();
    }
}
