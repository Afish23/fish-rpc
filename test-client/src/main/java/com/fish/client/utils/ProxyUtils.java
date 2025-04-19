package com.fish.client.utils;

import com.fish.rpc.factory.SingletonFactory;
import com.fish.rpc.proxy.RpcClientProxy;
import com.fish.rpc.transmission.RpcClient;
import com.fish.rpc.transmission.socket.client.SocketRpcClient;

public class ProxyUtils {
    private static final RpcClient rpcClient = SingletonFactory.getInstance(SocketRpcClient.class);
    private static final RpcClientProxy rpcClientProxy = new RpcClientProxy(rpcClient);

    public static <T> T getProxy(Class<T> clazz) {
        return rpcClientProxy.getProxy(clazz);
    }
}
