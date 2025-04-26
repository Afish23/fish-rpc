package com.fish.api;

import com.fish.rpc.annotation.Breaker;
import com.fish.rpc.annotation.Retry;

public interface UserService {
    @Retry
    @Breaker(windowTime = 300000)
    User getUser(Long id);
}
