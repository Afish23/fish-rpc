package com.fish.client;

import com.fish.api.User;
import com.fish.api.UserService;
import com.fish.client.utils.ProxyUtils;

public class Main {
    public static void main(String[] args) {
        UserService userService = ProxyUtils.getProxy(UserService.class);
        User user = userService.getUser(1L);
        System.out.println(user);

    }

}
