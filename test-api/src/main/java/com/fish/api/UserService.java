package com.fish.api;

import com.fish.rpc.annotation.Retry;

public interface UserService {
    @Retry
    User getUser(Long id);
}
