package org.example.rpc.transmission.socket.server;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.rpc.config.RpcServiceConfig;
import org.example.rpc.dto.RpcRequest;
import org.example.rpc.dto.RpcResp;
import org.example.rpc.handler.RpcReqHandler;
import org.example.rpc.provider.ServiceProvider;
import org.example.rpc.provider.impl.SimpleServiceProvider;
import org.example.rpc.transmission.RpcServer;
import org.example.rpc.util.ThreadPoolUtils;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class SocketRpcServer implements RpcServer {
    private final int port;
    private final RpcReqHandler rpcReqHandler;
    private final ServiceProvider serviceProvider;
    private final ExecutorService executor;

    public SocketRpcServer(int port) {
        this(port, new SimpleServiceProvider());
    }

    public SocketRpcServer(int port, SimpleServiceProvider serviceProvider) {
        this.port = port;
        this.serviceProvider = serviceProvider;
        this.rpcReqHandler = new RpcReqHandler(serviceProvider);
        this.executor = ThreadPoolUtils.createIoIntensiveThreadPool("socket-rpc-server-");
    }
    @Override
    public void start() {
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
