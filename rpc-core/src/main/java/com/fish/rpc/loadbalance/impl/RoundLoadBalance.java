package com.fish.rpc.loadbalance.impl;

import com.fish.rpc.dto.RpcReq;
import com.fish.rpc.loadbalance.LoadBalance;

import java.util.List;

/**
 * @author Afish
 * @date 2025/4/27 11:45
 */
public class RoundLoadBalance implements LoadBalance {
    private int last = -1;

    @Override
    public String select(List<String> list, RpcReq rpcReq) {
        last++;
        last = last % list.size();
        return list.get(last);
    }
}
