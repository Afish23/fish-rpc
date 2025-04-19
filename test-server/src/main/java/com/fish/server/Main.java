package com.fish.server;

import com.fish.rpc.config.RpcServiceConfig;
import com.fish.rpc.transmission.RpcServer;
import com.fish.rpc.transmission.socket.server.SocketRpcServer;
import com.fish.server.service.UserServiceImpl;

public class Main {
    public static void main(String[] args) {
        RpcServiceConfig config = new RpcServiceConfig(new UserServiceImpl());

        RpcServer rpcServer = new SocketRpcServer();
        rpcServer.publishService(config);
        rpcServer.start();

    }
}
