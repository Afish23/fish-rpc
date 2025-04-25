package com.fish.rpc.transmission.socket.client;

import com.fish.rpc.factory.SingletonFactory;
import com.fish.rpc.registry.ServiceDiscovery;
import com.fish.rpc.registry.impl.ZkServiceDiscovery;
import lombok.extern.slf4j.Slf4j;
import com.fish.rpc.dto.RpcReq;
import com.fish.rpc.dto.RpcResp;
import com.fish.rpc.transmission.RpcClient;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Slf4j
public class SocketRpcClient implements RpcClient {
    private final ServiceDiscovery serviceDiscovery;

    public SocketRpcClient(){
        this(SingletonFactory.getInstance(ZkServiceDiscovery.class));
    }

    public SocketRpcClient(ServiceDiscovery serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery;
    }

    @Override
    public Future<RpcResp<?>> sendReq(RpcReq rpcReq) {
        InetSocketAddress address = serviceDiscovery.lookupService(rpcReq);
        try(Socket socket = new Socket(address.getAddress(), address.getPort())){
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(rpcReq);
            outputStream.flush();
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            Object o = inputStream.readObject();

            return CompletableFuture.completedFuture((RpcResp<?>) o);

        }catch (Exception e){
            log.error("发送rpc请求失败", e);
            throw new RuntimeException(e);
        }
    }
}
