package org.example.rpc.transmission;

import org.example.rpc.config.RpcServiceConfig;

public interface RpcServer {
    void start();
    void publishService(RpcServiceConfig config);
}
