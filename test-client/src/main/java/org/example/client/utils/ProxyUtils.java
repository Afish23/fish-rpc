package org.example.client.utils;

import org.example.rpc.proxy.RpcClientProxy;
import org.example.rpc.transmission.RpcClient;
import org.example.rpc.transmission.socket.client.SocketRpcClient;

public class ProxyUtils {
    private static final RpcClient rpcClient = new SocketRpcClient("127.0.0.1", 8888);
    private static final RpcClientProxy rpcClientProxy = new RpcClientProxy(rpcClient);

    public static <T> T getProxy(Class<T> clazz) {
        return rpcClientProxy.getProxy(clazz);
    }
}
