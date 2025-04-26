package com.fish.rpc.proxy;

import cn.hutool.core.util.IdUtil;
import com.fish.rpc.annotation.Breaker;
import com.fish.rpc.annotation.Retry;
import com.fish.rpc.breaker.CircuitBreaker;
import com.fish.rpc.breaker.CircuitBreakerManager;
import com.fish.rpc.config.RpcServiceConfig;
import com.fish.rpc.dto.RpcReq;
import com.fish.rpc.dto.RpcResp;
import com.fish.rpc.enums.RpcRespStatus;
import com.fish.rpc.exception.RpcException;
import com.fish.rpc.transmission.RpcClient;
import com.github.rholder.retry.Retryer;
import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;
import com.github.rholder.retry.WaitStrategies;
import lombok.SneakyThrows;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Objects;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class RpcClientProxy implements InvocationHandler {
    private final RpcClient rpcClient;
    private final RpcServiceConfig config;

    public RpcClientProxy(RpcClient rpcClient){
        this(rpcClient, new RpcServiceConfig());
    }

    public RpcClientProxy(RpcClient rpcClient, RpcServiceConfig config) {
        this.rpcClient = rpcClient;
        this.config = config;
    }

    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(
                clazz.getClassLoader(),
                new Class[]{clazz}, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcReq rpcReq = RpcReq.builder()
                .reqId(IdUtil.fastSimpleUUID())
                .interfaceName(method.getDeclaringClass().getCanonicalName())
                .methodName(method.getName())
                .params(args)
                .paramTypes(method.getParameterTypes())
                .version(config.getVersion())
                .group(config.getGroup())
                .build();

        Breaker breaker = method.getAnnotation(Breaker.class);
        if (Objects.isNull(breaker)) {
            return sendReqWithRetry(rpcReq, method);
        }

        CircuitBreaker circuitBreaker = CircuitBreakerManager.get(rpcReq.rpcServiceName(), breaker);
        if (!circuitBreaker.canReq()){
            throw new RpcException("已被熔断处理");
        }

        try {
            Object o = sendReqWithRetry(rpcReq, method);
            circuitBreaker.success();
            return o;
        } catch (Exception e) {
            circuitBreaker.fail();
            throw e;
        }
    }

    @SneakyThrows
    private Object sendReqWithRetry(RpcReq rpcReq, Method method) {
        Retry retry = method.getAnnotation(Retry.class);
        if(Objects.isNull(retry)){
            return sendReq(rpcReq);
        }
        Retryer<Object> retryer = RetryerBuilder.newBuilder()
                .retryIfExceptionOfType(retry.value())
                .withStopStrategy(StopStrategies.stopAfterAttempt(retry.maxAttempts()))
                .withWaitStrategy(WaitStrategies.fixedWait(retry.delay(), TimeUnit.MILLISECONDS))
                .build();

        return retryer.call(()->sendReq(rpcReq));
    }

    @SneakyThrows
    private Object sendReq(RpcReq rpcReq)  {
        Future<RpcResp<?>> future = rpcClient.sendReq(rpcReq);
        RpcResp<?> rpcResp = future.get();
        check(rpcReq, rpcResp);
        return rpcResp.getData();
    }

    private void check(RpcReq rpcReq, RpcResp<?> rpcResp) {
        if(Objects.isNull(rpcResp)){
            throw new RpcException("rpcResp为空");
        }

        if(!Objects.equals(rpcReq.getReqId(), rpcResp.getReqId())){
            throw new RpcException("请求和响应的id不一致");
        }
        if(RpcRespStatus.isFailed(rpcResp.getCode())){
            throw new RpcException("响应值为失败：" + rpcResp.getMessage());

        }
    }
}
