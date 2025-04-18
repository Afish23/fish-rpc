package org.example.rpc.handler;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.rpc.dto.RpcRequest;
import org.example.rpc.provider.ServiceProvider;
import org.example.rpc.provider.impl.SimpleServiceProvider;

import java.lang.reflect.Method;

@Slf4j
public class RpcReqHandler {
    private final ServiceProvider serviceProvider;

    public RpcReqHandler(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    @SneakyThrows
    public Object invoke(RpcRequest rpcReq) {
        String rpcServiceName = rpcReq.rpcServiceName();
        Object service = serviceProvider.getService(rpcServiceName);

        log.debug("获取到对应服务： {}", service.getClass().getCanonicalName());
        Method method = service.getClass().getMethod(rpcReq.getMethodName(), rpcReq.getParamTypes());
        return method.invoke(service, rpcReq.getParams());
    }
}
