package org.example.server;

import org.example.api.User;
import org.example.api.UserService;
import org.example.rpc.config.RpcServiceConfig;
import org.example.rpc.proxy.RpcClientProxy;
import org.example.rpc.transmission.RpcServer;
import org.example.rpc.transmission.socket.server.SocketRpcServer;
import org.example.server.service.UserServiceImpl;

public class Main {
    public static void main(String[] args) {
        RpcServiceConfig config = new RpcServiceConfig(new UserServiceImpl());

        RpcServer rpcServer = new SocketRpcServer(8888);
        rpcServer.publishService(config);
        rpcServer.start();

    }
}
