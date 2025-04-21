package com.fish.client;

import com.fish.api.User;
import com.fish.api.UserService;
//import com.fish.client.utils.ProxyUtils;
import com.fish.rpc.dto.RpcReq;
import com.fish.rpc.transmission.RpcClient;
import com.fish.rpc.transmission.netty.client.NettyRpcClient;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
//        UserService userService = ProxyUtils.getProxy(UserService.class);
//        ExecutorService executorService = Executors.newFixedThreadPool(10);
//        for (int i = 0; i < 10; i++) {
//            executorService.execute(()->{
//                User user = userService.getUser(1L);
//                System.out.println(user);
//            });
//        }

        RpcClient rpcClient = new NettyRpcClient();
        rpcClient.sendReq(RpcReq.builder().interfaceName("请求数据").build());
    }

}
