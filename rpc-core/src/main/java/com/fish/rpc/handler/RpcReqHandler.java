package com.fish.rpc.handler;

import com.fish.rpc.annotation.Limit;
import com.fish.rpc.exception.RpcException;
import com.google.common.util.concurrent.RateLimiter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import com.fish.rpc.dto.RpcReq;
import com.fish.rpc.provider.ServiceProvider;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Slf4j
public class RpcReqHandler {
    private final ServiceProvider serviceProvider;
    private static final Map<String, RateLimiter> RATE_LIMITER_MAP = new ConcurrentHashMap<>();

    public RpcReqHandler(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    @SneakyThrows
    public Object invoke(RpcReq rpcReq) {
        String rpcServiceName = rpcReq.rpcServiceName();
        Object service = serviceProvider.getService(rpcServiceName);

        log.debug("获取到对应服务： {}", service.getClass().getCanonicalName());
        Method method = service.getClass().getMethod(rpcReq.getMethodName(), rpcReq.getParamTypes());
        Limit limit = method.getAnnotation(Limit.class);
        if (Objects.isNull(limit)) {
            return method.invoke(service, rpcReq.getParams());
        }
        RateLimiter rateLimiter = RATE_LIMITER_MAP.computeIfAbsent(rpcServiceName, __ ->
                RateLimiter.create(limit.permitsPerSecond()));
        if (!rateLimiter.tryAcquire(limit.timeout(), TimeUnit.MILLISECONDS)) {
            throw new RpcException("系统繁忙，请稍后重试");
        }
        return method.invoke(service, rpcReq.getParams());
    }
}
