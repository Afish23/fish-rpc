package com.fish.rpc.compress;


/**
 * @author Afish
 * @date 2025/4/22 17:26
 */
public interface Compress {
    byte[] compress(byte[] data);

    byte[] decompress(byte[] data);
}
