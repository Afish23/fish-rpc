package com.fish.rpc.transmission;

import com.fish.rpc.dto.RpcReq;
import com.fish.rpc.dto.RpcResp;

import java.util.concurrent.Future;

public interface RpcClient {
    Future<RpcResp<?>> sendReq(RpcReq req);
}
