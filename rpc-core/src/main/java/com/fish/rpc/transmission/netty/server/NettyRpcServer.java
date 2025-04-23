package com.fish.rpc.transmission.netty.server;

import com.fish.rpc.config.RpcServiceConfig;
import com.fish.rpc.constant.RpcConstant;
import com.fish.rpc.factory.SingletonFactory;
import com.fish.rpc.provider.ServiceProvider;
import com.fish.rpc.provider.impl.ZkServiceProvider;
import com.fish.rpc.transmission.RpcServer;
import com.fish.rpc.transmission.netty.codec.NettyRpcDecode;
import com.fish.rpc.transmission.netty.codec.NettyRpcEncode;
import com.fish.rpc.util.ShutdownHookUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Afish
 * @date 2025/4/21 10:52
 */
@Slf4j
public class NettyRpcServer implements RpcServer {
    private final ServiceProvider serviceProvider;
    private final int port;

    public NettyRpcServer(){
        this(RpcConstant.SERVER_PORT);
    }

    public NettyRpcServer(ServiceProvider serviceProvider) {
        this(serviceProvider, RpcConstant.SERVER_PORT);
    }

    public NettyRpcServer(int port) {
        this(SingletonFactory.getInstance(ZkServiceProvider.class), port);
    }

    public NettyRpcServer(ServiceProvider serviceProvider, int port) {
        this.serviceProvider = serviceProvider;
        this.port = port;
    }

    @Override
    public void start() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    //疑问
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            channel.pipeline().addLast(new NettyRpcDecode());
                            channel.pipeline().addLast(new NettyRpcEncode());
                            channel.pipeline().addLast(new NettyRpcServerHandler(serviceProvider));
                        }
                    });
            ShutdownHookUtils.clearAll();
            ChannelFuture channelFuture = bootstrap.bind(port).sync();
            log.info("NettyRpcServer已启动, 端口：{}", port);
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("服务端异常", e);
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    @Override
    public void publishService(RpcServiceConfig config) {
        serviceProvider.publishService(config);
    }
}
