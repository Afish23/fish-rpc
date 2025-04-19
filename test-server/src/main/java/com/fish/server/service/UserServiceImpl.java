package com.fish.server.service;

import com.fish.api.User;
import com.fish.api.UserService;

public class UserServiceImpl implements UserService {

    @Override
    public User getUser(Long id) {
        return User.builder()
                .id(++id)
                .name("张三")
                .build();
    }
}
