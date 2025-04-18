package org.example.rpc.proxy;

import cn.hutool.core.util.IdUtil;
import org.example.rpc.config.RpcServiceConfig;
import org.example.rpc.dto.RpcRequest;
import org.example.rpc.dto.RpcResp;
import org.example.rpc.enums.RpcRespStatus;
import org.example.rpc.exception.RpcException;
import org.example.rpc.transmission.RpcClient;
import org.example.rpc.transmission.socket.client.SocketRpcClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Objects;

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
        RpcClient rpcClient = new SocketRpcClient("127.0.0.1", 8888);
        RpcRequest rpcRequest = RpcRequest.builder()
                .requestId(IdUtil.fastSimpleUUID())
                .interfaceName(method.getDeclaringClass().getCanonicalName())
                .methodName(method.getName())
                .params(args)
                .paramTypes(method.getParameterTypes())
                .version(config.getVersion())
                .group(config.getGroup())
                .build();

        RpcResp<?> rpcResp = rpcClient.sendRequest(rpcRequest);
        check(rpcRequest, rpcResp);
        return rpcResp.getData();
    }

    private void check(RpcRequest rpcRequest, RpcResp<?> rpcResp) {
        if(Objects.isNull(rpcResp)){
            throw new RpcException("rpcResp为空");
        }

        if(!Objects.equals(rpcRequest.getRequestId(), rpcResp.getRequestId())){
            throw new RpcException("请求和响应的id不一致");
        }
        if(RpcRespStatus.isFailed(rpcResp.getCode())){
            throw new RpcException("响应值为失败：" + rpcResp.getMessage());

        }
    }
}
