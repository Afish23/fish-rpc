package org.example.rpc.transmission;

import org.example.rpc.dto.RpcRequest;
import org.example.rpc.dto.RpcResp;

public interface RpcClient {
    RpcResp<?> sendRequest(RpcRequest request);
}
