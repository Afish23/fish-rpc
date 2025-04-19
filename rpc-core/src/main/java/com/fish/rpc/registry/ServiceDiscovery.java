package com.fish.rpc.registry;

import com.fish.rpc.dto.RpcReq;

import java.net.InetSocketAddress;

/**
 * @author Afish
 * @date 2025/4/19 16:21
 */
public interface ServiceDiscovery {
    InetSocketAddress lookupService(RpcReq rpcReq);
}
