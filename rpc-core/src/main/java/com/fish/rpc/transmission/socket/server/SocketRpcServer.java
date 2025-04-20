package com.fish.rpc.transmission.socket.server;

import com.fish.rpc.constant.RpcConstant;
import com.fish.rpc.factory.SingletonFactory;
import com.fish.rpc.provider.impl.ZkServiceProvider;
import com.fish.rpc.util.ShutdownHookUtils;
import lombok.extern.slf4j.Slf4j;
import com.fish.rpc.config.RpcServiceConfig;
import com.fish.rpc.handler.RpcReqHandler;
import com.fish.rpc.provider.ServiceProvider;
import com.fish.rpc.provider.impl.SimpleServiceProvider;
import com.fish.rpc.transmission.RpcServer;
import com.fish.rpc.util.ThreadPoolUtils;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

@Slf4j
public class SocketRpcServer implements RpcServer {
    private final int port;
    private final RpcReqHandler rpcReqHandler;
    private final ServiceProvider serviceProvider;
    private final ExecutorService executor;

    public SocketRpcServer(){
        this(RpcConstant.SERVER_PORT);
    }

    public SocketRpcServer(int port) {
        this(port, SingletonFactory.getInstance(ZkServiceProvider.class));
    }

    public SocketRpcServer(int port, ServiceProvider serviceProvider) {
        this.port = port;
        this.serviceProvider = serviceProvider;
        this.rpcReqHandler = new RpcReqHandler(serviceProvider);
        this.executor = ThreadPoolUtils.createIoIntensiveThreadPool("socket-rpc-server-");
    }
    @Override
    public void start() {
        ShutdownHookUtils.clearAll();
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            log.info("服务启动，端口： {}", port);
            Socket socket;
            while ((socket = serverSocket.accept()) != null) {
                executor.submit(new SocketReqHandler(socket, rpcReqHandler));
            }
        }catch (Exception e) {
            log.error("服务端异常", e);
        }
    }

    @Override
    public void publishService(RpcServiceConfig config) {
        serviceProvider.publishService(config);
    }
}
