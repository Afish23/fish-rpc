package com.fish.rpc.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @author Afish
 * @date 2025/4/21 16:42
 */
@ToString
@Getter
@AllArgsConstructor
public enum MsgType {
    HEARTBEAT_REQ((byte) 1,"心跳请求"),
    HEARTBEAT_RESP((byte) 2,"心跳响应"),
    RPC_REQ((byte) 3,"rpc请求"),
    RPC_RESP((byte) 4,"rpc响应");

    private final byte code;
    private final String desc;
}
