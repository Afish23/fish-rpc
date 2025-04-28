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
import com.fish.rpc.util.ConfigUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author Afish
 * @date 2025/4/21 10:50
 */
@Slf4j
public class NettyRpcClient implements RpcClient {
    private static final Bootstrap bootstrap;
    private static final int DEFAULT_CONNECT_TIMEOUT_MILLIS = 5000;
    private final ServiceDiscovery serviceDiscovery;
    private final ChannelPool channelPool;

    public NettyRpcClient() {
        this(SingletonFactory.getInstance(ZkServiceDiscovery.class));
    }

    public NettyRpcClient(ServiceDiscovery serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery;
        this.channelPool = SingletonFactory.getInstance(ChannelPool.class);
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
                        channel.pipeline().addLast(new IdleStateHandler(0, 5, 0, TimeUnit.SECONDS));
                        channel.pipeline().addLast(new NettyRpcDecode());
                        channel.pipeline().addLast(new NettyRpcEncode());
                        channel.pipeline().addLast(new NettyRpcClientHandler());
                    }
                });
    }

    @SneakyThrows
    @Override
    public Future<RpcResp<?>> sendReq(RpcReq req) {
        CompletableFuture<RpcResp<?>> cf = new CompletableFuture<>();
        UnprocessedRpcReq.put(req.getReqId(), cf);

        InetSocketAddress address = serviceDiscovery.lookupService(req);
        Channel channel = channelPool.get(address, () -> connect(address));
        log.info("nettyRpcClient连接到: {}", address);

        String serializer = ConfigUtils.getRpcConfig().getSerializer();

        RpcMsg rpcMsg = RpcMsg.builder()
                .version(VersionType.VERSION1)
                .serializeType(SerializeType.from(serializer))
                .compressType(CompressType.GZIP)
                .msgType(MsgType.RPC_REQ)
                .data(req)
                .build();
        channel.writeAndFlush(rpcMsg).addListener((ChannelFutureListener) listener -> {
            if (!listener.isSuccess()) {
                listener.channel().close();
                cf.completeExceptionally(listener.cause());
            }
        });

        return cf;
    }

    private Channel connect(InetSocketAddress address) {
        try {
            return bootstrap.connect(address).sync().channel();
        } catch (InterruptedException e) {
            log.error("连接到远程服务器失败", e);
            throw new RuntimeException(e);
        }
    }
}
