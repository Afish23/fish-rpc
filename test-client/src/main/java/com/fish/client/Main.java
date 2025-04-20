package com.fish.client;

import com.fish.api.User;
import com.fish.api.UserService;
import com.fish.client.utils.ProxyUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        UserService userService = ProxyUtils.getProxy(UserService.class);
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            executorService.execute(()->{
                User user = userService.getUser(1L);
                System.out.println(user);
            });
        }

    }

}
