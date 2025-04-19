package com.fish.rpc.registry.impl;

import cn.hutool.core.util.StrUtil;
import com.fish.rpc.constant.RpcConstant;
import com.fish.rpc.factory.SingletonFactory;
import com.fish.rpc.registry.ServiceRegistry;
import com.fish.rpc.registry.zk.ZkClient;
import com.fish.rpc.util.IPUtils;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @author Afish
 * @date 2025/4/19 17:29
 */
@Slf4j
public class ZkServiceRegistry implements ServiceRegistry {
    private final ZkClient zkClient;

    public ZkServiceRegistry() {
        this(SingletonFactory.getInstance(ZkClient.class));
    }

    public ZkServiceRegistry(ZkClient zkClient) {
        this.zkClient = zkClient;
    }

    @Override
    public void registerService(String rpcServiceName, InetSocketAddress address) {
        log.info("服务注册, rpcServiceName: {}, address: {}", rpcServiceName, address);

        String path = RpcConstant.ZK_RPC_ROOT_PATH + StrUtil.SLASH + rpcServiceName + StrUtil.SLASH + IPUtils.toIpPort(address);
        zkClient.createPersistentNode(path);
    }
}
