package com.fish.rpc.registry;

import java.net.InetSocketAddress;

/**
 * @author Afish
 * @date 2025/4/19 16:20
 */
public interface ServiceRegistry {
    void registerService(String rpcServiceName, InetSocketAddress address);
}
