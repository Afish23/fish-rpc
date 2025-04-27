package com.fish.rpc.loadbalance;

import com.fish.rpc.dto.RpcReq;

import java.util.List;

/**
 * @author Afish
 * @date 2025/4/19 17:47
 */
public interface LoadBalance {
    public String select(List<String> list, RpcReq rpcReq);
}
