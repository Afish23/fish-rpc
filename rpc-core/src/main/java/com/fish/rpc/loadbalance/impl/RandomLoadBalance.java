package com.fish.rpc.loadbalance.impl;

import cn.hutool.core.util.RandomUtil;
import com.fish.rpc.loadbalance.LoadBalance;

import java.util.List;

/**
 * @author Afish
 * @date 2025/4/19 17:49
 */
public class RandomLoadBalance implements LoadBalance {
    @Override
    public String select(List<String> list) {
        return RandomUtil.randomEle(list);
    }
}
