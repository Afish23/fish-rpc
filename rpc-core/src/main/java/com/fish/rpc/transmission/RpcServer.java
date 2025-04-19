package com.fish.rpc.transmission;

import com.fish.rpc.config.RpcServiceConfig;

public interface RpcServer {
    void start();
    void publishService(RpcServiceConfig config);
}
