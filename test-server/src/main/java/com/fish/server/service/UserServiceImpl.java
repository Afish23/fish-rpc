package com.fish.server.service;

import com.fish.api.User;
import com.fish.api.UserService;
import com.fish.rpc.annotation.Limit;

public class UserServiceImpl implements UserService {
    @Limit(permitsPerSecond = 5, timeout = 0)
    @Override
    public User getUser(Long id) {
        return User.builder()
                .id(++id)
                .name("张三")
                .build();
    }
}
