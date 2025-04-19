package com.fish.rpc.registry.impl;

import cn.hutool.core.util.StrUtil;
import com.fish.rpc.constant.RpcConstant;
import com.fish.rpc.dto.RpcReq;
import com.fish.rpc.factory.SingletonFactory;
import com.fish.rpc.loadbalance.LoadBalance;
import com.fish.rpc.loadbalance.impl.RandomLoadBalance;
import com.fish.rpc.registry.ServiceDiscovery;
import com.fish.rpc.registry.zk.ZkClient;
import com.fish.rpc.util.IPUtils;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author Afish
 * @date 2025/4/19 17:42
 */
public class ZkServiceDiscovery implements ServiceDiscovery {
    private final ZkClient zkClient;
    private final LoadBalance loadBalance;

    public ZkServiceDiscovery() {
        this(
                SingletonFactory.getInstance(ZkClient.class),
                SingletonFactory.getInstance(RandomLoadBalance.class)
        );
    }

    public  ZkServiceDiscovery(ZkClient zkClient, LoadBalance loadBalance) {
        this.zkClient = zkClient;
        this.loadBalance = loadBalance;
    }

    @Override
    public InetSocketAddress lookupService(RpcReq rpcReq) {
        String path = RpcConstant.ZK_RPC_ROOT_PATH + StrUtil.SLASH + rpcReq.rpcServiceName();

        List<String> children = zkClient.getChildrenNode(path);
        String address = loadBalance.select(children);
        return IPUtils.toInetSocketAddress(address);
    }
}
