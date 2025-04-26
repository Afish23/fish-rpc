package com.fish.client;

import com.fish.api.User;
import com.fish.api.UserService;
import com.fish.client.utils.ProxyUtils;
import com.fish.rpc.dto.RpcReq;
import com.fish.rpc.transmission.RpcClient;
import com.fish.rpc.transmission.netty.client.NettyRpcClient;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        UserService userService = ProxyUtils.getProxy(UserService.class);
        User user = userService.getUser(1L);
        System.out.println(user);

        try {
            TimeUnit.SECONDS.sleep(1);
        }catch (InterruptedException e){
            throw new RuntimeException(e);
        }
    }

}
