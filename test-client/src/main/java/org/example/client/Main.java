package org.example.client;

import org.example.api.User;
import org.example.api.UserService;
import org.example.client.utils.ProxyUtils;
import org.example.rpc.dto.RpcRequest;
import org.example.rpc.dto.RpcResp;
import org.example.rpc.proxy.RpcClientProxy;
import org.example.rpc.transmission.RpcClient;
import org.example.rpc.transmission.socket.client.SocketRpcClient;
import org.example.rpc.util.ThreadPoolUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        UserService userService = ProxyUtils.getProxy(UserService.class);
        User user = userService.getUser(1L);
        System.out.println(user);

    }

}
