package com.fish.rpc.loadbalance;

import java.util.List;

/**
 * @author Afish
 * @date 2025/4/19 17:47
 */
public interface LoadBalance {
    public String select(List<String> list);
}
