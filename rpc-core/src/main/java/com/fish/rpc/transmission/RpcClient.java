package com.fish.rpc.transmission;

import com.fish.rpc.dto.RpcReq;
import com.fish.rpc.dto.RpcResp;

public interface RpcClient {
    RpcResp<?> sendReq(RpcReq req);
}
