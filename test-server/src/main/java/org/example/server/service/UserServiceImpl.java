package org.example.server.service;

import cn.hutool.core.util.IdUtil;
import org.example.api.User;
import org.example.api.UserService;

public class UserServiceImpl implements UserService {

    @Override
    public User getUser(Long id) {
        return User.builder()
                .id(++id)
                .name("张三")
                .build();
    }
}
