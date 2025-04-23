package com.fish.rpc.constant;

/**
 * @author Afish
 * @date 2025/4/19 16:31
 */
public class RpcConstant {
    public static final int SERVER_PORT = 8888;
    public static final String ZK_IP = "localhost";
    public static final int ZK_PORT = 2181;
    public static final String ZK_RPC_ROOT_PATH = "/fish-rpc";
    public static final String NETTY_RPC_KEY = "RpcResp";
    public static final byte[] RPC_MAGIC_CODE = new byte[] {(byte) 'f', (byte) 'r', (byte) 'p', (byte) 'c'};
    public static final int REQ_HEAD_LEN = 16;
    public static final int REQ_MAx_LEN = 1024 * 1024 * 8;
}
